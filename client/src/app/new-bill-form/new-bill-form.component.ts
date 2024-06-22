import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import {
  FormBuilder,
  FormsModule,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { BillService } from '../../services/bill/bill.service';
import { Bill } from '../../model/Bill';
import { HttpStatusCode } from '@angular/common/http';
import { BillCategoryCode } from '../../constants/Categories';
import { DecimalPipe } from '@angular/common';
import { Button } from 'primeng/button';
import { DropdownModule } from 'primeng/dropdown';
import { SliderChangeEvent, SliderModule } from 'primeng/slider';
import { InputTextModule } from 'primeng/inputtext';
import { CardModule } from 'primeng/card';
import { BlockUIModule } from 'primeng/blockui';
import { TranslateModule } from '@ngx-translate/core';
import { isValidCategory, isValidDescription } from '../../utils/formValidationUtils';
import { Router } from '@angular/router';
import { TranslationService } from '../../services/translation/translation.service';

@Component({
    selector: 'app-new-bill-form',
    templateUrl: './new-bill-form.component.html',
    styleUrls: ['./new-bill-form.component.css'],
    standalone: true,
  imports: [BlockUIModule, CardModule, FormsModule, ReactiveFormsModule, InputTextModule, SliderModule, DropdownModule, Button, DecimalPipe, TranslateModule]
})
export class NewBillFormComponent implements OnInit {
  categories: { label: string, value: BillCategoryCode }[] = [];
  submitButtonIsDisabled: boolean;
  blocked: boolean;
  username: string | null;
  @Output() formEmitter = new EventEmitter<boolean>();
  ownShareOfBill: number;
  billFormBuilder = this.formBuilder.group({
    amount: [null, [Validators.required, Validators.min(1)]],
    category: [null, [Validators.required, isValidCategory]],
    description: ['', [Validators.required, isValidDescription]],
    sliderPercent: 50
  })
  sliderPercent = 50;

  constructor(private formBuilder: FormBuilder,
              private billService: BillService,
              private router: Router,
              private translationService: TranslationService
  ) {
    this.blocked = false;
    this.submitButtonIsDisabled = false;
  }

  ngOnInit(): void {
    this.username = localStorage.getItem('name');
    this.billFormBuilder.valueChanges.subscribe(value => {
      if (!value.amount) return
      this.ownShareOfBill = Math.round((value.amount * (this.sliderPercent / 100)) * 100) / 100;
    })

    this.translationService.getTranslatedCategories().subscribe(translatedCategories => {
      this.categories = translatedCategories;
    });
  }

  onSubmit(): void {
    this.submitButtonIsDisabled = true;
    this.blocked = true;
    const {amount, category, description} = this.billFormBuilder.value;
    const formIsNotValid = !this.billFormBuilder.valid || !amount || !category || !description;
    if (formIsNotValid || !this.username) {
      this.resetState();
      return;
    }

    const bill = new Bill(amount, category, description, this.ownShareOfBill, this.username)

    this.billService.createBill(bill).subscribe((response) => {
      if (response.status === HttpStatusCode.Created) {
        this.formEmitter.emit(true)
        this.resetForm();
        this.billService.notifyBillCreated(amount);
        this.router.navigate(['bills'])
      } else {
        this.formEmitter.emit(false);
      }
      this.submitButtonIsDisabled = false;
      this.blocked = false;
    });
  }

  handleSliderChange(e: SliderChangeEvent): void {
    if (e.value === undefined) return;
    this.sliderPercent = e.value
  }

  resetForm(): void {
    this.billFormBuilder.reset();
    this.billFormBuilder.get('sliderPercent')?.setValue(50);
    this.ownShareOfBill = 0;
  }
  
  isControlInvalid(controlName: string): boolean {
    const control = this.billFormBuilder.get(controlName);
    return control ? control.invalid && control.touched : false;
  }
  
  resetState() {
    this.formEmitter.emit(false);
    this.submitButtonIsDisabled = false;
    this.blocked = false;
    this.billFormBuilder.markAllAsTouched();
  }

}
