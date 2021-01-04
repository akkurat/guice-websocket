import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GameopeningComponent } from './gameopening.component';

describe('GameopeningComponent', () => {
  let component: GameopeningComponent;
  let fixture: ComponentFixture<GameopeningComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GameopeningComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GameopeningComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
