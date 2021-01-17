import { Statement } from '@angular/compiler';
import { Injectable, OnDestroy } from '@angular/core';
import { Observable } from 'rxjs';
import { Subscription } from 'stompjs';
import { StompService } from '../stomp.service';
import { ICardLegal } from './game/game.component';

@Injectable()
export class JassServiceService implements OnDestroy {
  sink: any;
  observable: Observable<State>;
  gameId: string;

  constructor(
    private stomp: StompService
  ) {

    this.observable = new Observable(obs => {
      this.sink = obs
    });

  }


  subscribe(gameId: string) {
    this.gameId = gameId
    this.stomp.subscribe('/user/game/play/' + gameId,
      message => this.handleGameEvent(message.body))
      .then(s => this.gameSubscription = s)

    return this.observable;
  }

  gameSubscription: Subscription;


  state = new State()


  handleGameEvent(payload: string) {
    console.log('gameevent')
    if (payload === "ERROR") {
      // this.router.navigateByUrl('/')
      return;
    }
    this.state.gameEvents.push(payload)
    this.handlePayload(payload)
  }

  popGameEvent() {
    if (this.state.gameEvents.length > 0) {
      const payload = this.state.gameEvents.shift()
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
      this.state.users = obj.payload;
      this.sink.next({users: this.state.users})
    }
  }

  private handleModeEvent(payload: ModePayload) {
    this.state.modi = payload.modes;
    this.state.cards = payload.cards
    this.state.table = []

    this.sink.next(this.state)

  }

  private handleTurn(p: StatusPayload) {
    // this.cards = this.enrichCards(p.availCards, p.legalCards)
    // this.addToTable(p.roundCards);
    this.sink.next(p.availCards)
    this.state.points = p.points;
    if (this.state.log.length != p.gameInfoPoints.length) { this.state.log = p.gameInfoPoints }
    this.state.mode = p.mode;
    this.state.modi = null;
    this.state.yourTurn = p.yourTurn;

    this.sink.next(this.state)
  }

  playCard(card) {
    // if (this.canPlay) {
    debugger
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
}

export class State {
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
  log: ImmutableRound[] = []
  mode: TrickMode;
  waiting: boolean;
  params: any;
  canPlay = false
}