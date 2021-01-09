import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RoundinfoComponent } from './roundinfo.component';

describe('RoundinfoComponent', () => {
  let component: RoundinfoComponent;
  let fixture: ComponentFixture<RoundinfoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RoundinfoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RoundinfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
