import { StompService } from '@/stomp.service';
import { AfterViewChecked, Component, ErrorHandler, Input, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'stompjs';

@Component({
  selector: 'jas-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.less']
})
export class GameComponent implements OnInit, OnDestroy {
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





  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private stomp: StompService
  ) {
  }

  async ngOnInit(): Promise<void> {
    this.params = this.route.params;
    console.log("init game")
    // interval(500).subscribe( () => this.popGameEvent())
    this.gameSubscription = await this.stomp.subscribe('/user/game/play/' + this.params.value.id,
      message => this.handleGameEvent(message.body)
    );
  }

  handleGameEvent(payload: string) {
    console.log('gameevent')
    if (payload === "ERROR") {
      this.router.navigateByUrl('/')
      return;
    }
    this.gameEvents.push(payload)
    this.handlePayload(payload)
  }
  popGameEvent() {
    if (this.gameEvents.length > 0) {
      const payload = this.gameEvents.shift()
      this.handlePayload(payload);
    }
  }
  private handlePayload(payload: any) {
    const obj = JSON.parse(payload);
    if (obj.code === 'TURN' || obj.code === 'STATUS') {
      this.handleTurn(obj.payload as StatusPayload);
    } else if (obj.code === 'MODE') {
      this.handleModeEvent(obj.payload as ModePayload);
    } else if (obj.code === 'USERS') {
      this.users = obj.payload;
    }
  }

  private handleModeEvent(payload: ModePayload) {
    this.modi = payload.modes;
    this.cards = payload.cards
    this.table = []
  }

  private handleTurn(p: StatusPayload) {
    this.cards = this.enrichCards(p.availCards, p.legalCards)
    this.addToTable(p.roundCards);
    this.points = p.points;
    if(this.log.length != p.gameInfoPoints.length) {this.log = p.gameInfoPoints}
    this.mode = p.mode;
    this.modi = null;
    this.yourTurn = p.yourTurn;
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

  playCard(card) {
    if (this.canPlay) {
        console.log('Clicked to play card')
        this.stomp.send('/app/cmds/play/' + this.params.value.id, {}, JSON.stringify({ code: 'PLAY', payload: card }))
      
    };
  }

  selectMode(mode) {
    this.stomp.send('/app/cmds/decide/' + this.params.value.id, {}, JSON.stringify({ code: 'DECIDE', payload: { type: mode } }))
  }

  ngOnDestroy() {
    this.gameSubscription?.unsubscribe()
  }

  jumpTimeOut() {
    this.createTimeout()
    this.waiting = false
  }

  get canPlay() {
    return this.yourTurn
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
}



export interface ICardLegal extends JassCard {
  legal?: boolean
}


