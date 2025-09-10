import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowChartComponent } from './show-chart.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideZonelessChangeDetection } from '@angular/core';

describe('ShowChartComponent', () => {
  let component: ShowChartComponent;
  let fixture: ComponentFixture<ShowChartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowChartComponent],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideZonelessChangeDetection()],
    })
      .compileComponents();

    fixture = TestBed.createComponent(ShowChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
