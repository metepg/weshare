import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchBillsComponent } from './search-bills.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { TranslateModule } from '@ngx-translate/core';
import { MessageService } from 'primeng/api';
import { provideNoopAnimations } from '@angular/platform-browser/animations';

describe('SearchBillsComponent', () => {
  let component: SearchBillsComponent;
  let fixture: ComponentFixture<SearchBillsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SearchBillsComponent, TranslateModule.forRoot()],
      // eslint-disable-next-line @typescript-eslint/no-deprecated
      providers: [provideHttpClient(), provideHttpClientTesting(), MessageService, provideNoopAnimations()],
    })
      .compileComponents();

    fixture = TestBed.createComponent(SearchBillsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
