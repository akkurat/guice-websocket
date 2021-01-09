import { StompService } from '@/stomp.service';
import { Component, Input, OnDestroy, OnInit } from '@angular/core';
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
  cards: ICard[] = []
  table = []
  points: any;
  mode: any;
  modi: any;
  infopoints: any;
  cardBuffer = []
  lastCardEvent: number = 0;
  h: NodeJS.Timeout;
  yourTurn = '';  


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
      this.modi = obj.payload;
    }
    this.gameEvents.push(payload)
  }

  private handleTurn(p: any) {
    this.cards = p.availCards;
    this.addToTable(p.roundCards);
    this.points = p.points;
    this.infopoints = p.gameInfoPoints;
    this.mode = p.mode;
    this.modi = null;
    this.yourTurn = p.yourTurn;
  }
  private addToTable(roundCards: any) {
    this.cardBuffer.push(roundCards);
    if (this.cardBuffer.length == 1) {
      if (Date.now() - this.lastCardEvent > 6000) {
        this.popCards()
      } else {
        this.createTimeout( )
      }
    }
  }
  private popCards() {
    if(this.cardBuffer.length > 0 ) {
      this.lastCardEvent = Date.now()
      this.table = this.cardBuffer.shift()
    }
  }

  private createTimeout() {
    const to = this.cardBuffer[0].length == 0 ? 6000 : 200
    this.h = setTimeout(() => {
      this.popCards()
      if (this.cardBuffer.length > 0) {
        this.createTimeout()
      }
    }, to)
  }

  get gameEventsString() {
    return JSON.stringify(this.gameEvents);
  }

  playCard(card) {
    this.stomp.send('/app/cmds/play/' + this.params.value.id, {}, JSON.stringify({ code: 'PLAY', payload: card }))
  }

  selectMode(mode) {
    this.stomp.send('/app/cmds/decide/' + this.params.value.id, {}, JSON.stringify({ code: 'DECIDE', payload: { type: mode } }))
  }

  ngOnDestroy() {
    this.gameSubscription?.unsubscribe()
  }

  jumpTimeOut() {
    if(this.h) {
      window.clearTimeout( this.h )
      this.popCards()
      if( this.cardBuffer.length > 0 ) {
        this.createTimeout()
      }
    }
  }
}


