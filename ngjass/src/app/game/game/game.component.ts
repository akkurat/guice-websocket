import { StompService } from '@/stomp.service';
import { Component, ErrorHandler, Input, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { asyncScheduler, queueScheduler } from 'rxjs';
import { QueueScheduler } from 'rxjs/internal/scheduler/QueueScheduler';
import { Subscription } from 'stompjs';
import { ICard } from '../jassinterfaces';

@Component({
  selector: 'jas-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.less']
})
export class GameComponent implements OnInit, OnDestroy {
  params: any;
  gameSubscription: Subscription;
  gameEvents = [];
  cards: ICardLegal[] = []
  table = []
  points: any;
  mode: any;
  modi: any;
  infopoints: any;
  cardBuffer = []
  lastCardEvent: number = 0;
  h: NodeJS.Timeout;
  yourTurn = '';
  waiting: any;
  users: any;


  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private stomp: StompService
  ) { }

  async ngOnInit(): Promise<void> {
    this.params = this.route.params;
    console.log("init game")
    this.gameSubscription = await this.stomp.subscribe('/user/game/play/' + this.params.value.id,
      message => this.handleGameEvent(message.body)
    );
  }

  handleGameEvent(payload) {
    if (payload === "ERROR") {
      this.router.navigateByUrl('/')
      return;
    }
    const obj = JSON.parse(payload);
    if (obj.code === 'TURN' || obj.code === 'STATUS') {
      this.handleTurn(obj.payload);
    } else if (obj.code === 'MODE') {
      this.handleModeEvent(obj.payload)
    } else if (obj.code ==='USERS') {
      this.users = obj.payload
    }
    this.gameEvents.push(payload)
  }
  handleModeEvent(payload: any) {
      this.modi = payload.modes;
      this.cards = payload.cards
  }

  private handleTurn(p: any) {
    this.cards = this.enrichCards(p.availCards,p.legalCards)
    this.addToTable(p.roundCards);
    this.points = p.points;
    this.infopoints = p.gameInfoPoints;
    this.mode = p.mode;
    this.modi = null;
    this.yourTurn = p.yourTurn;
  }
  private addToTable(roundCards: any) {
     if (roundCards.length === 0 || this.cardBuffer.length > 0 ) {
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
    if (this.cardBuffer.length > 0) {
      setTimeout(() => {
        this.popCards()
        this.createTimeout()
      }, 10)
    }
  }

  get gameEventsString() {
    return JSON.stringify(this.gameEvents);
  }

  playCard(card) {
    if(this.canPlay) {
      this.stomp.send('/app/cmds/play/' + this.params.value.id, {}, JSON.stringify({ code: 'PLAY', payload: card }))
    }
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
    return this.cardBuffer.length == 0;
  }

  private enrichCards(hand: ICard[], legal: ICard[]): ICardLegal[]{
    const lookup = new Set(legal.map(this.mapCard))
    return hand.map(c => ({...c, legal: lookup.has(this.mapCard(c))}))
  }
  private mapCard(c: ICard): string {
    return c.color+c.value;
  }
}



export interface ICardLegal extends ICard{
  legal: boolean
}


