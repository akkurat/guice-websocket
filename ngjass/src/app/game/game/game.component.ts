import { StompService } from '@/stomp.service';
import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
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
  cards : ICard[]= []
  table = []
  points: any;
  mode: any;
  modi: any;
  infopoints: any;

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
    if(payload === "ERROR") {
      this.router.navigateByUrl('/')
      return;
    }
    const obj = JSON.parse(payload);
    if( obj.code === 'TURN' || obj.code === 'STATUS') {
      this.cards = obj.payload.availCards;
      this.table = obj.payload.roundCards;
      this.points = obj.payload.points;
      this.infopoints = obj.payload.gameInfoPoints
      this.mode = obj.payload.mode;
    } else if (obj.code === 'MODE' ) {
        this.modi = obj.payload;
    }
    this.gameEvents.push(payload)
  }

  get gameEventsString() {
    return JSON.stringify(this.gameEvents);
  }

  playCard(card) {
    this.stomp.send('/app/cmds/play/'+this.params.value.id, {}, JSON.stringify({code: 'PLAY', payload: card}))
  }

  selectMode(mode) {
    this.stomp.send('/app/cmds/decide/'+this.params.value.id, {}, JSON.stringify({code: 'DECIDE', payload: {type: mode}}))
  }

  ngOnDestroy() {
    this.gameSubscription?.unsubscribe()
  }
}


