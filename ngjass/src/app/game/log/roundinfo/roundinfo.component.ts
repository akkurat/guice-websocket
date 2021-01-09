import { colormap } from '@/game/card/card.component';
import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';

@Component({
  selector: 'jas-roundinfo',
  templateUrl: './roundinfo.component.html',
  styleUrls: ['./roundinfo.component.less']
})
export class RoundinfoComponent implements OnChanges {

  @Input('info') _info: IRoundInfo 
  factor: number;
  color: {};
  caption: string;
  constructor() { }
  ngOnChanges(changes: SimpleChanges): void {
    if(this._info) {
      const i = this._info
      this.factor = i.factor
      // if(i.semanticInfo) {
      //   const s = i.semanticInfo 
      //   if(s.symbol) {
      //     this.symbol = 
      //   }
      //   if(s.color) {
      //     this.color = colormap[this._info.color]
      //   }
      this.caption = i.caption;

    }
  }



}

export interface IRoundInfo {
  semanticInfo?: any;
  color?: string;
  factor: number;
  caption: string;
}