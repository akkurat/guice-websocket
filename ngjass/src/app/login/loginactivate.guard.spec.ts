import { TestBed } from '@angular/core/testing';

import { LoginactivateGuard } from './loginactivate.guard';

describe('LoginactivateGuard', () => {
  let guard: LoginactivateGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(LoginactivateGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
