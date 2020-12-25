import { Injectable, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import  * as SockJS  from 'sockjs-client'
import * as Stomp from 'stompjs'
import { Client } from 'stompjs';

@Injectable({
  providedIn: 'root'
})
export class StompService {
  constructor() {
    this.openWebSocketConnection();
  }
  private client: Client;
  private waitingList: Function[] = []
  private getWebSocket(): WebSocket  {
    return new SockJS("/api/stomp");
    // return new WebSocket("ws://"+window.location.host+"/api/stomp");
  }
  openWebSocketConnection() {
    const webSocket = this.getWebSocket();
    this.client = Stomp.over(webSocket);
    this.client.connect({}, () => {
        this.waitingList.forEach( f => f() )
        this.waitingList = []
    });
    this.client.subscribe
  }

  /**
   * May not be called before openWebSocketConnection
   * but handles waiting for the connection 
   * @param filter 
   * @param handler 
   */
  subscribe( filter: string, handler: (item) => void) : Promise<Stomp.Subscription> {
    const promise = new Promise<Stomp.Subscription>((resolve,reject) => {
    this.waitingList.push(
      () => resolve(this.client.subscribe(filter, handler))
    )
    if(this.client?.connected) {
        this.waitingList.forEach( f => f() )
        this.waitingList = []
    }
  });
    return promise;
  }
  send(filter: string, add: {}, text: string) {
      this.client.send(filter, add, text)
  }



}
