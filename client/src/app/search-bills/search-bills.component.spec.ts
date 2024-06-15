import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchBillsComponent } from './search-bills.component';

describe('SearchBillsComponent', () => {
  let component: SearchBillsComponent;
  let fixture: ComponentFixture<SearchBillsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SearchBillsComponent]
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
