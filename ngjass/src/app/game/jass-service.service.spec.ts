import { TestBed } from '@angular/core/testing';

import { JassServiceService } from './jass-service.service';

describe('JassServiceService', () => {
  let service: JassServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(JassServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
