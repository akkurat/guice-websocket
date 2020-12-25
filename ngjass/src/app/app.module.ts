import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import {WebsocketService} from './websocket.service';
import {StompService} from './stomp.service'
import { SpringconnectComponent } from './springconnect/springconnect.component'
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    AppComponent,
    SpringconnectComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [WebsocketService, StompService],
  bootstrap: [AppComponent]
})
export class AppModule { }
