import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowBillsComponent } from './show-bills.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { MessageService } from 'primeng/api';

describe('ShowBillsComponent', () => {
  let component: ShowBillsComponent;
  let fixture: ComponentFixture<ShowBillsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowBillsComponent],
      providers: [provideHttpClient(), provideHttpClientTesting(), MessageService],
    })
      .compileComponents();

    fixture = TestBed.createComponent(ShowBillsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
