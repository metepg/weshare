import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewBillComponent } from './new-bill.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ConfirmationService, MessageService } from 'primeng/api';
import { TranslateModule } from '@ngx-translate/core';

describe('NewBillComponent', () => {
  let component: NewBillComponent;
  let fixture: ComponentFixture<NewBillComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NewBillComponent, TranslateModule.forRoot()],
      providers: [provideHttpClient(), provideHttpClientTesting(), MessageService, ConfirmationService],
    })
      .compileComponents();

    fixture = TestBed.createComponent(NewBillComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
