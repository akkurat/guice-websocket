import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import {WebsocketService} from './websocket.service';
import {StompService} from './stomp.service'
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { GameComponent } from './game/game/game.component';

import { LoginModule } from './login/login.module'
import { LobbyModule } from './lobby';
import { GameModule } from './game';

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    LoginModule,
    GameModule,
    LobbyModule
  ],
  providers: [WebsocketService, StompService],
  bootstrap: [AppComponent]
})
export class AppModule { }
