import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';

@Component({
  selector: 'jas-log',
  templateUrl: './log.component.html',
  styleUrls: ['./log.component.less']
})
export class LogComponent implements OnChanges {

  @Input('log') _log: ImmutableRound[] = []

  log: PresenterRound[] = []

  details: PresenterRound = null

  constructor() { }
  ngOnChanges(changes: SimpleChanges): void {
    if (this._log) {
      this.log = this._log.map(this.map)
    }
  }

  private map(t: ImmutableRound): PresenterRound  {
    const gagi = { 
      totalPointsByTeam: t.totalPointsByTeam,
      turns: t.parametrizedTurns,
      roundInfo: t.parmeterizedRound
    }
    return gagi
  }

  showDetail(round: PresenterRound) {
    if (this.details === round) {
      this.details = null
    } else {
      this.details = round
    }
  }


}
export class PresenterRound {
  totalPointsByTeam: { [index: string]: number };
  turns: ImmutableTrick[];
  roundInfo: IParmeterizedRound;
}


