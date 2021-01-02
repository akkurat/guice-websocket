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

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private stomp: StompService
  ) { }

  async ngOnInit(): Promise<void> {
    this.params = this.route.params;
    try {
    this.gameSubscription = await this.stomp.subscribe('/user/game/play/' + this.params.value.id,
      message => this.handleGameEvent(message.body)
      );
    } catch {
    }
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
    }
    this.gameEvents.push(payload)
  }

  playCard(card) {
    this.stomp.send('/app/cmds/play/'+this.params.value.id, {}, JSON.stringify({code: 'PLAY', payload: card}))
  }

  ngOnDestroy() {
    this.gameSubscription?.unsubscribe()
  }
}


