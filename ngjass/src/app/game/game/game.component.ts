import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription, asyncScheduler, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { JassServiceService } from '../jass-service.service'

@Component({
  selector: 'jas-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.less'],
  providers: [ JassServiceService ]

})
export class GameComponent implements OnInit {
  cards: ICardLegal[] = []
  cardBuffer = []
  lastCardEvent: number = 0;
  yourTurn = false;
  users: Observable<UserPayload>;
  modi: { [index: string]: PresenterMode; };
  table: ImmutableLogEntry[];
  gameEvents: string[] = [];
  points: { [index: string]: number; };
  mode: TrickMode;
  waiting: boolean;
  params: any;
  canPlay = true;
  modes: Observable<ModePayload>;
  waitingTask: Subscription;
  log: Observable<ImmutableRound[]>;





  constructor(
    private route: ActivatedRoute,
    private jassService: JassServiceService
  ) {
  }

  async ngOnInit(): Promise<void> {
    this.params = this.route.params;
    console.log("init game")
    this.users = this.jassService.users
    this.jassService.modes.subscribe( m => {this.modi = m.modes; this.cards = m.cards} )
    this.jassService.tricks.subscribe(s => {
       this.addToTable(s.table)
       this.cards = s.cards
       this.yourTurn = s.yourTurn
       this.mode = s.mode
    })
    this.log = this.jassService.gameinfos.pipe(map(s => s.log))

    this.jassService.subscribe(this.params.value.id)

    // interval(500).subscribe( () => this.popGameEvent())
  }


  selectMode(key) {
    this.jassService.selectMode(key);
  }


  trackCard(card:ICardLegal) {
    return card.color+card.value+card.legal
  }

  playCard(c: JassCard) {
    this.jassService.playCard(c);
  }

  private addToTable(roundCards: ImmutableLogEntry[]) {
    if (roundCards.length === 0 || this.cardBuffer.length > 0) {
      this.waiting = true
      if(this.waitingTask) {this.waitingTask.unsubscribe()}
      this.waitingTask = asyncScheduler.schedule(() => this.jumpTimeOut(), 5000)
      this.cardBuffer.push(roundCards);
    } else {
      this.table = roundCards
    }
  }

  private popCards() {
    if (this.cardBuffer.length > 0) {
      this.lastCardEvent = Date.now()
      this.table = this.cardBuffer.shift()
    }
  }

  jumpTimeOut() {
    if (this.waitingTask) { this.waitingTask.unsubscribe() }
    while (this.cardBuffer.length > 0) {
      this.popCards()
    }
    this.waiting = false
  }
}



export interface ICardLegal extends JassCard {
  legal?: boolean
}


