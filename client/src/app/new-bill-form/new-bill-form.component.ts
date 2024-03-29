import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BillService } from '../../services/bill/bill.service';
import { Bill } from '../../model/Bill';
import { HttpStatusCode } from '@angular/common/http';
import {BillCategoryCode} from '../../utils/Categories';

interface FormValidationStrategies {
  [key: string]: (value: any) => boolean;
}

@Component({
  selector: 'app-new-bill-form',
  templateUrl: './new-bill-form.component.html',
  styleUrls: ['./new-bill-form.component.css']
})
export class NewBillFormComponent implements OnInit {
  submitButtonIsDisabled: boolean;
  blocked: boolean;
  @Input() username: string
  @Output() formEmitter = new EventEmitter<boolean>();
  ownShareOfBill: number;
  billFormBuilder = this.formBuilder.group({
    amount: [null, Validators.required],
    category: [null, Validators.required],
    description: ['', Validators.required],
    sliderPercent: 50
  })

  categories = Object.keys(BillCategoryCode)
    .filter((key) => isNaN(Number(key)))
    .map((key, index) => ({
      label: key,
      value: index
    }));
  sliderPercent = 50;

  constructor(private formBuilder: FormBuilder, private billService: BillService) {
    this.blocked = false;
    this.submitButtonIsDisabled = false;
  }

  ngOnInit(): void {
    this.billFormBuilder.valueChanges.subscribe(value => {
      if (!value.amount) return
      this.ownShareOfBill = Math.round((value.amount * (this.sliderPercent / 100)) * 100) / 100;
    })
  }

  onSubmit(): void {
    this.submitButtonIsDisabled = true;
    this.blocked = true;
    const isValid: boolean = this.validateForm((this.billFormBuilder))
    if (!isValid) {
      this.formEmitter.emit(false);
      this.submitButtonIsDisabled = false;
      this.blocked = false;
      return;
    }

    const {amount, category, description} = this.billFormBuilder.value
    const bill = new Bill(amount!, category!, description!, this.ownShareOfBill, this.username)

    this.billService.createBill(bill).subscribe((response) => {
      if (response.status === HttpStatusCode.Created) {
        this.formEmitter.emit(true)
        this.resetForm();
      } else {
        this.formEmitter.emit(false);
      }
      this.submitButtonIsDisabled = false;
      this.blocked = false;
    });
  }
  validateForm(form: FormGroup): boolean {
    const strategies: FormValidationStrategies = {
      sliderPercent: () => true,
      amount: (value: number) => value > 0,
      category: (value: number) => value >= 0,
      description: (value: string) => value.toString().trim() !== '',
    };

    for (const [key, value] of Object.entries(form.value)) {
      const isValid = strategies[key];
      if (!isValid(value)) return false;
    }
    return true;
  }

  handleSliderChange(e: any): void {
    this.sliderPercent = e.value;
  }

  resetForm(): void {
    this.billFormBuilder.reset();
    this.billFormBuilder.get('sliderPercent')?.setValue(50);
    this.ownShareOfBill = 0;
  }

}
