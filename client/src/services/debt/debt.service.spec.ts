import { TestBed } from '@angular/core/testing';

import { DebtService } from './debt.service';
import { provideZonelessChangeDetection } from '@angular/core';

describe('DebtService', () => {
  let service: DebtService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideZonelessChangeDetection()]
    });
    service = TestBed.inject(DebtService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
