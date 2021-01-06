import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'jas-log',
  templateUrl: './log.component.html',
  styleUrls: ['./log.component.less']
})
export class LogComponent implements OnInit {

  @Input() log: []

  constructor() { }

  ngOnInit(): void {
  }

}
