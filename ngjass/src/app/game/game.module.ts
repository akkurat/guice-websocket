import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GameComponent } from './game/game.component'
import { ChatComponent, ChatModule } from '@/chat';
import { CardComponent } from './card/card.component'



@NgModule({
  declarations: [ GameComponent, CardComponent, CardComponent],
  imports: [
    CommonModule, 
    ChatModule
  ],
  exports: [ GameComponent ]
})
export class GameModule { }

export { GameComponent }