import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LobbyComponent } from './lobby/lobby.component'
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [LobbyComponent],
  imports: [
    CommonModule, FormsModule
  ],
  exports: [
    LobbyComponent
  ]
})
export class LobbyModule { }
