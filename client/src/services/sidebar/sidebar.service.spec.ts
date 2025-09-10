import { TestBed } from '@angular/core/testing';

import { SidebarService } from './sidebar.service';
import { provideZonelessChangeDetection } from '@angular/core';

describe('SidebarService', () => {
  let service: SidebarService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideZonelessChangeDetection()]
    });
    service = TestBed.inject(SidebarService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
