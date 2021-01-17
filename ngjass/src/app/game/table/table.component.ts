import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';

@Component({
  selector: 'jas-table[info]',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.less']
})

export class TableComponent implements OnChanges {
  uiPlayers: Shit[]

  constructor() { }

  ngOnChanges(changes: SimpleChanges): void {
    console.log(changes)
    if(this.info) {
      this.convertUiPlayers()
    }
  }

  @Input()
  cards: ImmutableLogEntry[]

  @Input()
  info: UserPayload

  @Output() confirm = new EventEmitter()

  get message() {
    return this.cards?.length == 4 ? '...' : 'Your Turn...'
  }

  convertUiPlayers() {
    const p = this.info.playerList
    
    const pos = ['bottom', 'right', 'top', 'left']

    const myIndex = p.findIndex( r => r.ref == this.info.youAre )

    const retValue :Shit[]=[] 

    

    for( const [idx,v] of pos.entries()) {
      const player = p[(idx+4-myIndex)%4]
      let card: ImmutableLogEntry
      if( this.cards ) {
        card = this.cards.find( l => l.playerReference.ref == player.ref)
      }
      retValue.push( {class: v, player, card: card?.card, time: card?.dateTime} )
    }
    this.uiPlayers = retValue;
  }


}

export interface Shit {
  class: string;
  player: IPlayerReference;
  card?: JassCard;
  time?: any

}