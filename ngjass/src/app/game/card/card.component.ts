import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ICardLegal } from '../game/game.component';

@Component({
  selector: 'jas-card[card]',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.less']
})
export class CardComponent implements OnInit {

  @Input() card: JassCard

  
  get blackOrRed() {
    switch (this.card.color) {
      case "KREUZ":
      case "SCHAUFEL":
        return "black";
      case "ECKEN":
      case "HERZ":
        return "red";
    }
  }

  constructor() { }

  get color_() {
    return colormap[this.card.color]
  }

  get rank_() {
    return this.card.value.replace("_","")
  }

  ngOnInit(): void {
    console.log(this.card)
  }

}

export const colormap = {
  "KREUZ": "♣",
  "SCHAUFEL": "♠",
  "HERZ": "♥",
  "ECKEN": "♦"
}

