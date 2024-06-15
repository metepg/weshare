import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewBillFormComponent } from './new-bill-form.component';

describe('NewBillComponent', () => {
  let component: NewBillFormComponent;
  let fixture: ComponentFixture<NewBillFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
    imports: [NewBillFormComponent]
})
    .compileComponents();

    fixture = TestBed.createComponent(NewBillFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
