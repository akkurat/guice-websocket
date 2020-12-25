import { Component, inject, OnInit  } from '@angular/core';
import { WebsocketService } from './websocket.service';
import { Subject } from 'rxjs'

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.less']
})
export class AppComponent implements OnInit {
  private echoChannel: Subject<MessageEvent|any>;
  messages: string[] = []
  chatmessages: {o:string, m:string}[] = []
  constructor( private ws: WebsocketService) {}
  ngOnInit(): void {
    // Connect to WS
    this.echoChannel = this.ws.connect("ws://"+window.location.host+"/api/echo")
    this.echoChannel.subscribe( msg => {this.parseMessage(msg.data)})
    this.echoChannel.next({"type":"INFO",params:{"username":"observer"}})
  }
  parseMessage(data: any) {
    this.messages.push(data)

    const obj = JSON.parse(data) as IMessage
    if( obj.type == 'BC' || obj.type == 'DM')
    {
      this.chatmessages.push({o: obj.uref.caption, m: obj.content})
    }
  }

  click(ev) {
    console.log(ev)
    this.echoChannel.next({"type":"BC","content":"asfsadf"})
  }

  createGame() {
    this.echoChannel.next({"type": "CMD", "content": "NEW_GAME"})
  }

  title = 'ngjass';

}

interface IMessage {
  type: 'BC' | 'DM'
  uref: { ref: string, caption: string }
  content: string
}