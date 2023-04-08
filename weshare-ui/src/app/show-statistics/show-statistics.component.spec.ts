import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowStatisticsComponent } from './show-statistics.component';

describe('ShowStatisticsComponent', () => {
  let component: ShowStatisticsComponent;
  let fixture: ComponentFixture<ShowStatisticsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowStatisticsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowStatisticsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
