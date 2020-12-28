import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import {WebsocketService} from './websocket.service';
import {StompService} from './stomp.service'
import { SpringconnectComponent } from './springconnect/springconnect.component'
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { LoginComponent } from './login/login.component';
import { GameComponent } from './game/game.component';

@NgModule({
  declarations: [
    AppComponent,
    SpringconnectComponent,
    LoginComponent,
    GameComponent
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
