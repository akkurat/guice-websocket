import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SpringconnectComponent } from './springconnect.component';

describe('SpringconnectComponent', () => {
  let component: SpringconnectComponent;
  let fixture: ComponentFixture<SpringconnectComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SpringconnectComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SpringconnectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
