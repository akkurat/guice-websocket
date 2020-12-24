import { Injectable } from "@angular/core";
import {Observable, Observer, Subject} from 'rxjs'

@Injectable()
export class WebsocketService {
  constructor() {}

  private subject: Subject<MessageEvent>;
  private initialBuffer = []

  public connect(url): Subject<MessageEvent> {
    if (!this.subject) {
      this.subject = this.create(url);
      console.log("Successfully connected: " + url);
    }
    return this.subject;
  }

  private create(url): Subject<MessageEvent> {
    let ws = new WebSocket(url);

    let observable = Observable.create((obs: Observer<MessageEvent>) => {
      ws.onmessage = obs.next.bind(obs);
      ws.onerror = obs.error.bind(obs);
      ws.onclose = obs.complete.bind(obs);
      return ws.close.bind(ws);
    });
    let observer = {
      next: (data: Object) => {
        if (ws.readyState === WebSocket.OPEN) {
          ws.send(JSON.stringify(data));
        } else if(ws.readyState === ws.CONNECTING ){
          ws.addEventListener("open", ev => ws.send(JSON.stringify(data)) )
        } else {
          console.error("Closed or closing", ws.readyState)
        }
      }
    };
    return Subject.create(observer, observable);
  }
}
