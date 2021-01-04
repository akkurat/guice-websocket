import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LobbyComponent } from './lobby/lobby.component'
import { FormsModule } from '@angular/forms';
import { GametetypeComponent } from './gametetype/gametetype.component';
import { GameopeningComponent } from './gameopening/gameopening.component';

@NgModule({
  declarations: [LobbyComponent, GametetypeComponent, GameopeningComponent],
  imports: [
    CommonModule, FormsModule
  ],
  exports: [
    LobbyComponent
  ]
})
export class LobbyModule { }
