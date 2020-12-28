import { TestBed } from '@angular/core/testing';

import { JasslogicService } from './jasslogic.service';

describe('JasslogicService', () => {
  let service: JasslogicService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(JasslogicService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
