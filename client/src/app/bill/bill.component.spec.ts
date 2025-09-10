import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BillComponent } from './bill.component';
import { MOCK_BILL } from '../../testUtils/constants';
import { provideZonelessChangeDetection } from '@angular/core';

describe('BillComponent', () => {
  let component: BillComponent;
  let fixture: ComponentFixture<BillComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BillComponent],
      providers: [provideZonelessChangeDetection()]
    }).compileComponents();

    fixture = TestBed.createComponent(BillComponent);
    component = fixture.componentInstance;

    fixture.componentRef.setInput('user', { id: 1, name: 'Test User' });
    fixture.componentRef.setInput('bill', MOCK_BILL);

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
