import { StompService } from '@/stomp.service';
import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'stompjs';

@Component({
  selector: 'jas-gametetype',
  templateUrl: './gametetype.component.html',
  styleUrls: ['./gametetype.component.less']
})
export class GametetypeComponent implements OnInit, OnDestroy {
  gametypes: {};
  subscription: Subscription;

  constructor(private stomp: StompService) { }

  ngOnInit(): void {
      this.stomp.subscribe("/game/gametypes", (item) => {
        this.gametypes = JSON.parse(item.body)
      }).then(s => this.subscription = s);
  }

  ngOnDestroy() {
    this.subscription?.unsubscribe();
  }

  createGame(type: string) {
    this.stomp.send("/app/cmds/new", {}, JSON.stringify({type}))
  }

}

export interface IGameType {

}