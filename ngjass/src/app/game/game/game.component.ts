import { StompService } from '@/stomp.service';
import { AfterViewChecked, Component, ErrorHandler, Input, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'stompjs';
import { JassServiceService } from '../jass-service.service'

@Component({
  selector: 'jas-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.less'],
  providers: [ JassServiceService ]

})
export class GameComponent implements OnInit {
  gameSubscription: Subscription;
  cards: ICardLegal[] = []
  cardBuffer = []
  lastCardEvent: number = 0;
  h: NodeJS.Timeout;
  yourTurn = false;
  users: UserPayload;
  modi: { [index: string]: PresenterMode; };
  table: ImmutableLogEntry[];
  gameEvents: string[] = [];
  points: { [index: string]: number; };
  log: ImmutableRound[]=[]
  mode: TrickMode;
  waiting: boolean;
  params: any;
  canPlay = false;





  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private jassService: JassServiceService
  ) {
  }

  async ngOnInit(): Promise<void> {
    this.params = this.route.params;
    console.log("init game")
    this.jassService.subscribe(this.params.value.id)
    .subscribe( c => Object.assign(this, c))
    // interval(500).subscribe( () => this.popGameEvent())
  }


  private addToTable(roundCards: ImmutableLogEntry[]) {
    if (roundCards.length === 0 || this.cardBuffer.length > 0) {
      this.waiting = true
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

  private createTimeout() {
    while (this.cardBuffer.length > 0) {
      this.popCards()
    }
  }


  

  jumpTimeOut() {
    this.createTimeout()
    this.waiting = false
  }

  private enrichCards(hand: JassCard[], legal: JassCard[]): ICardLegal[] {
    const lookup = new Set(legal.map(this.mapCard))
    return hand.map(c => ({ ...c, legal: lookup.has(this.mapCard(c)) }))
  }
  private mapCard(c: JassCard): string {
    return c.color + c.value;
  }

  trackCard(idx,card:ICardLegal) {
    return card.color+card.value+card.legal
  }

  playCard(c: JassCard) {
    this.jassService.playCard(c);
  }
}



export interface ICardLegal extends JassCard {
  legal?: boolean
}


