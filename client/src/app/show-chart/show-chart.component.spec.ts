import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowChartComponent } from './show-chart.component';

describe('ShowStatisticsComponent', () => {
  let component: ShowChartComponent;
  let fixture: ComponentFixture<ShowChartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowChartComponent]
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
