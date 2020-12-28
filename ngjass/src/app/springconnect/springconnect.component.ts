import { Component, OnInit } from '@angular/core';
import { StompService } from '../stomp.service';
import { HttpClient } from '@angular/common/http'
import { IGame } from '../game/jassinterfaces'
import  * as Stomp  from 'stompjs'





@Component({
  selector: 'Springconnect',
  templateUrl: './springconnect.component.html',
  styleUrls: ['./springconnect.component.less'],
  
})
export class SpringconnectComponent implements OnInit {
  joinedGame: [string, unknown][];

  constructor(
    private stomp: StompService,
    private http: HttpClient

    ) { }

  public messages: string[] = []
  public cmds: string[] = []
  public games: IGame[] = []
  public gametypes = []
  private subscriptions: Stomp.Subscription[] = []

  public text: string

  ngOnInit(): void {
    this.openWebSocketConnection();
    this.http.get('/api/games').subscribe( e => console.log(e))
    this.http.get('/api/gametypes').subscribe( e => console.log(e))
    // this.initializeAuctionItems();
  }
  openWebSocketConnection() {
      this.stomp.subscribe("/chat/messages", (item) => {
        this.messages.push(item.body);
      }).then(s => this.subscriptions.push(s));
      this.stomp.subscribe("/game/cmds", (item) => {
        this.cmds.push(item.body)
      }).then(s => this.subscriptions.push(s));
      this.stomp.subscribe("/game/games", (item) => {
        this.games = JSON.parse(item.body)
      }).then(s => this.subscriptions.push(s));
      this.stomp.subscribe("/game/gametypes", (item) => {
        this.gametypes = Object.entries(JSON.parse(item.body))
      }).then(s => this.subscriptions.push(s));

      this.stomp.subscribe("/game/joined", (item) => {
        this.joinedGame = Object.entries(JSON.parse(item.body))
      }).then(s => this.subscriptions.push(s));

      this.stomp.subscribe("/user/game/joined", (item) => {
        this.joinedGame = Object.entries(JSON.parse(item.body))
      }).then(s => this.subscriptions.push(s));
      
  }

  sendCmd(w:string) {
    this.stomp.send("/app/cmds", {}, JSON.stringify({cmd: "PLAY", payload: {card: this.text} }))
  }
  sendMsg(w:string) {
    this.stomp.send("/app/cmds", {}, this.text)
  }

  createGame() {
    this.stomp.send("/app/cmds/new", {}, JSON.stringify({type: "all"}))
  }

  joinGame(id: string) {
    this.stomp.send("/app/cmds/join", {}, JSON.stringify({id: id}))
  }

  insertOrUpdateItem(arg0: any) {
    console.log(arg0)
  }

  ngOnDestroy() {
    this.closeWebSocketConnection();
  }
  closeWebSocketConnection() {
    this.subscriptions.forEach( s => s.unsubscribe())
  }

}
