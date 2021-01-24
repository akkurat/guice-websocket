import { Component, inject, OnInit  } from '@angular/core';
import { WebsocketService } from './websocket.service';
import { Subject } from 'rxjs'
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { CheckUserService } from './login/check-user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.less']
})
export class AppComponent implements OnInit {
  private echoChannel: Subject<MessageEvent|any>;
  messages: string[] = []
  chatmessages: {o:string, m:string}[] = []
  uname: string;
  constructor( private ws: WebsocketService, private http: HttpClient, public loginService: CheckUserService, private router: Router) {}
  ngOnInit(): void {
    const hos =  window.location.host;
    // Connect to WS
    this.echoChannel = this.ws.connect("ws://"+hos+"/api/echo")
    this.echoChannel.subscribe( msg => {this.parseMessage(msg.data)})
    this.echoChannel.next({"type":"INFO",params:{"username":"observer"}})

    this.loginService.isLoggedIn().subscribe( uname => this.uname = uname )

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
    this.echoChannel.next({"type":"BC","content":"asfsadf"})
  }

  createGame() {
    this.echoChannel.next({"type": "CMD", "content": "NEW_GAME"})
  }

  logout() {
    this.loginService.logout().then( r => this.router.navigateByUrl('/login') )
  }





  title = 'ngjass';

}

interface IMessage {
  type: 'BC' | 'DM'
  uref: { ref: string, caption: string }
  content: string
}