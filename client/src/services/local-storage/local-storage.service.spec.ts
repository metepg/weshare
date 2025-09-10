import { TestBed } from '@angular/core/testing';

import { LocalStorageService } from './local-storage.service';
import { provideZonelessChangeDetection } from '@angular/core';

describe('LocalStorageService', () => {
  let service: LocalStorageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideZonelessChangeDetection()]
    });
    service = TestBed.inject(LocalStorageService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
