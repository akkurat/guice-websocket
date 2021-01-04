import { Component, OnInit } from '@angular/core';
import { StompService } from '@/stomp.service';
import { HttpClient } from '@angular/common/http'
import { IGame } from '@/game/jassinterfaces'
import  * as Stomp  from 'stompjs'
import { Router } from '@angular/router';





@Component({
  selector: 'jas-lobby',
  templateUrl: './lobby.component.html',
  styleUrls: ['./lobby.component.less'],
  
})
export class LobbyComponent implements OnInit {
  joinedGame: [string, unknown][];

  constructor(
    private stomp: StompService,
    private router: Router

    ) { }

  public cmds: string[] = []
  public games: IGame[] = []
  private subscriptions: Stomp.Subscription[] = []

  public text: string

  ngOnInit(): void {
    this.openWebSocketConnection();
    console.log("init lobby")
  }
  openWebSocketConnection() {
      this.stomp.subscribe("/game/games", (item) => {
        console.log("games message")
        this.games = JSON.parse(item.body)
      }).then(s => this.subscriptions.push(s));

      this.stomp.subscribe("/user/game/joined", (game) => {
        console.log("joined message")
        this.reactToJoin(JSON.parse(game.body));
      }).then(s => this.subscriptions.push(s));
      
  }

  sendCmd(w:string) {
    this.stomp.send("/app/cmds", {}, JSON.stringify({cmd: "PLAY", payload: {card: this.text} }))
  }
  sendMsg(w:string) {
    this.stomp.send("/app/cmds", {}, this.text)
  }


  removeGame(id) {
    this.stomp.send("/app/cmds/remove", {}, JSON.stringify({gameId: id}))
  }

  joinGame(id: string) {
    this.stomp.send("/app/cmds/join", {}, JSON.stringify({id: id}))
  }

  reactToJoin(game) {
    this.router.navigate(['play', game.uuid])
  }

  insertOrUpdateItem(arg0: any) {
    console.log(arg0)
  }

  ngOnDestroy() {
    this.closeWebSocketConnection();
    console.log("lobby destroy")
  }
  closeWebSocketConnection() {
    this.subscriptions.forEach( s => s.unsubscribe())
  }

}
