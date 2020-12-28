import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.less']
})
export class GameComponent implements OnInit {
  params: any;

  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.params = this.route.params;

  }

}
