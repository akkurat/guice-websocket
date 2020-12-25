import { Component, OnInit } from '@angular/core';
import { StompService } from '../stomp.service';
import { HttpClient } from '@angular/common/http'
import  * as Stomp  from 'stompjs'





@Component({
  selector: 'Springconnect',
  templateUrl: './springconnect.component.html',
  styleUrls: ['./springconnect.component.less'],
  
})
export class SpringconnectComponent implements OnInit {

  constructor(
    private stomp: StompService,
    private http: HttpClient

    ) { }

  public messages: string[] = []
  public cmds: string[] = []
  public games: string[] = []
  public gametypes: string[] = []
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
        this.games.push(item.body)
      }).then(s => this.subscriptions.push(s));
      this.stomp.subscribe("/game/gametypes", (item) => {
        this.gametypes.push(item.body)
      }).then(s => this.subscriptions.push(s));
      
  }

  send(type: string) {
    this.stomp.send("/app/"+type, {}, this.text)
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
