import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GametetypeComponent } from './gametetype.component';

describe('GametetypeComponent', () => {
  let component: GametetypeComponent;
  let fixture: ComponentFixture<GametetypeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GametetypeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GametetypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
