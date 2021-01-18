import { EventEmitter, Injectable, OnDestroy } from '@angular/core';
import { BehaviorSubject, Observable, ReplaySubject, Subject, Subscriber } from 'rxjs';
import { share } from 'rxjs/operators';
import { Subscription } from 'stompjs';
import { StompService } from '../stomp.service';
import { ICardLegal } from './game/game.component';

@Injectable()
export class JassServiceService implements OnDestroy {
  gameId: string;

  private readonly modeSink = new Subject<ModePayload>();
  readonly modes = this.modeSink.asObservable()

  private readonly userSink = new ReplaySubject<UserPayload>(1);
  readonly users = this.userSink.asObservable()

  private readonly trickSink =  new Subject<ConvertedStatusPayload>();
  readonly tricks = this.trickSink.asObservable();

  private readonly gameInfoSink  =  new Subject<GameInfo>();
  readonly gameinfos = this.gameInfoSink.asObservable();

  constructor(
    private stomp: StompService
  ) {
  }


  subscribe(gameId: string) {
    this.gameId = gameId
    this.stomp.subscribe('/user/game/play/' + gameId,
      message => this.handleGameEvent(message.body))
      .then(s => this.gameSubscription = s)
  }

  gameSubscription: Subscription;


  state = new State()


  handleGameEvent(payload: string) {
    console.log('gameevent')
    if (payload === "ERROR") {
      // this.router.navigateByUrl('/')
      return;
    }
    this.handlePayload(payload)
  }

  private handlePayload(payload: any) {
    const obj = JSON.parse(payload);
    if (obj.code === 'TURN' || obj.code === 'STATUS') {
      this.handleTurn(obj.payload as StatusPayload);
    } else if (obj.code === 'MODE') {
      this.handleModeEvent(obj.payload as ModePayload);
    } else if (obj.code === 'USERS') {
      this.userSink.next(obj.payload)
    }
  }

  private handleModeEvent(payload: ModePayload) {
    this.state.cards = payload.cards
    this.state.table = []

    this.modeSink.next(payload)
  }

  gameInfo: GameInfo = { log : [], points:{} }
  private handleTurn(p: StatusPayload) {
    if (this.gameInfo.log.length != p.gameInfoPoints.length) { 
      this.gameInfoSink.next({points:p.points, log: p.gameInfoPoints})
    } else {
      this.gameInfoSink.next({points:p.points})
    }
    this.gameInfo.log = p.gameInfoPoints 
    this.gameInfo.points = p.points;

    this.state.table = p.roundCards
    this.state.cards = this.enrichCards(p.availCards, p.legalCards)
    this.state.mode = p.mode;
    this.state.yourTurn = p.yourTurn;

    this.trickSink.next(this.state)
  }

  playCard(card) {
    // if (this.canPlay) {
    console.log('Clicked to play card')
    this.stomp.send('/app/cmds/play/' + this.gameId, {}, JSON.stringify({ code: 'PLAY', payload: card }))
    // };
  }

  selectMode(mode) {
    this.stomp.send('/app/cmds/decide/' + this.gameId, {}, JSON.stringify({ code: 'DECIDE', payload: { type: mode } }))
  }
  ngOnDestroy() {
    this.gameSubscription?.unsubscribe()
  }

  private enrichCards(hand: JassCard[], legal: JassCard[]): ICardLegal[] {
    const lookup = new Set(legal.map(this.mapCard))
    return hand.map(c => ({ ...c, legal: lookup.has(this.mapCard(c)) }))
  }
  private mapCard(c: JassCard): string {
    return c.color + c.value;
  }
}


export class State {
  cards: ICardLegal[] = []
  lastCardEvent: number = 0;
  yourTurn = false;
  table: ImmutableLogEntry[];
  mode: TrickMode;
}

export interface ConvertedStatusPayload {
  cards: ICardLegal[]
  yourTurn: boolean
  table: ImmutableLogEntry[];
  mode: TrickMode;

}

export interface GameInfo {
  points: { [index: string]: number; };
  log?: ImmutableRound[]
}