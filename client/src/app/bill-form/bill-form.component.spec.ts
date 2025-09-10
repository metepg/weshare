import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BillFormComponent } from './bill-form.component';
import { TranslateModule } from '@ngx-translate/core';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ConfirmationService } from 'primeng/api';
import { provideZonelessChangeDetection } from '@angular/core';

describe('BillFormComponent', () => {
  let component: BillFormComponent;
  let fixture: ComponentFixture<BillFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BillFormComponent, TranslateModule.forRoot()],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideZonelessChangeDetection(), ConfirmationService],
    })
      .compileComponents();

    fixture = TestBed.createComponent(BillFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
