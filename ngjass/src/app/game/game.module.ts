import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GameComponent } from './game/game.component'
import { ChatComponent, ChatModule } from '@/chat';
import { CardComponent } from './card/card.component';
import { LogComponent } from './log/log.component';
import { RoundinfoComponent } from './log/roundinfo/roundinfo.component';
import { TableComponent } from './table/table.component'



@NgModule({
  declarations: [ GameComponent, CardComponent, CardComponent, LogComponent, RoundinfoComponent, TableComponent],
  imports: [
    CommonModule, 
    ChatModule
  ],
  exports: [ GameComponent ]
})
export class GameModule { }

export { GameComponent }