import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { IRoundInfo } from './roundinfo/roundinfo.component';

@Component({
  selector: 'jas-log',
  templateUrl: './log.component.html',
  styleUrls: ['./log.component.less']
})
export class LogComponent implements OnChanges {

  @Input('log') _log: any[] = []

  log: ImmutableRound[] = []

  details: ImmutableRound = null

  constructor() { }
  ngOnChanges(changes: SimpleChanges): void {
    if (this._log) {
      this.log = this._log.map(this.map)
    }
  }

  private map(t): ImmutableRound {
    return {
      totalPointsByTeam: t.totalPointsByTeam,
      turns: t.parametrizedTurns,
      roundInfo: t.parmeterizedRound
    }
  }

  showDetail(round: ImmutableRound) {
    if (this.details === round) {
      this.details = null
    } else {
      this.details = round
    }
  }


}


export interface ImmutableRound {
  totalPointsByTeam: {}
  turns: { log: {}, whoTakes: IPlayerReference}[]
  roundInfo: IRoundInfo
}

export interface IPlayerReference {
  ref: string
  team: { ref: string}
}


