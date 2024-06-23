import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { InputTextModule } from 'primeng/inputtext';
import { SliderChangeEvent, SliderModule } from 'primeng/slider';
import { DecimalPipe } from '@angular/common';
import { DropdownModule } from 'primeng/dropdown';
import { Button } from 'primeng/button';
import { Bill } from '../../model/Bill';
import { BillCategoryCode } from '../../constants/Categories';
import { isValidCategory, isValidDescription } from '../../utils/formValidationUtils';
import { TranslationService } from '../../services/translation/translation.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-bill-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    TranslateModule,
    InputTextModule,
    SliderModule,
    DecimalPipe,
    DropdownModule,
    Button
  ],
  templateUrl: './bill-form.component.html',
  styleUrl: './bill-form.component.scss'
})

export class BillFormComponent implements OnInit, OnDestroy {
  private subscription: Subscription;
  categories: { label: string, value: BillCategoryCode }[] = [];
  submitButtonIsDisabled: boolean;
  username: string | null;
  @Input() description = '';
  @Input() ownShareOfBill = 0;
  @Input() sliderPercent = 50;
  @Input() amount = 0;
  @Input() category = null;
  @Output() formEmitter = new EventEmitter<Bill>();
  billFormBuilder: FormGroup<{
    amount: FormControl<number | null>;
    category: FormControl<null>;
    description: FormControl<string | null>;
    sliderPercent: FormControl<number | null>;
  }>      

  constructor(private formBuilder: FormBuilder, private translationService: TranslationService) {
    this.submitButtonIsDisabled = true;
  }

  ngOnInit(): void {
    this.billFormBuilder = this.formBuilder.group({
      amount: [this.amount, [Validators.required, Validators.min(1)]],
      category: [this.category, [Validators.required, isValidCategory]],
      description: [this.description, [Validators.required, isValidDescription]],
      sliderPercent: this.sliderPercent
    })
    
    this.billFormBuilder.valueChanges.subscribe(value => {
      if (!value.amount) return
      this.ownShareOfBill = Math.round((value.amount * (this.sliderPercent / 100)) * 100) / 100;
    })

    this.subscription = this.translationService.getTranslatedCategories().subscribe(translatedCategories => {
      this.categories = translatedCategories;
    });
  }

  isControlInvalid(controlName: string): boolean {
    const control = this.billFormBuilder.get(controlName);
    return control ? control.invalid && control.touched : false;
  }

  onSubmit(): void {
    this.submitButtonIsDisabled = true;
    this.username = localStorage.getItem('name');
    const {amount, category, description} = this.billFormBuilder.value;
    const formIsNotValid = !this.billFormBuilder.valid || !amount || !category || !description;
    if (formIsNotValid || !this.username) {
      return;
    }
    this.formEmitter.emit(new Bill(amount, category, description, this.ownShareOfBill, this.username));
    this.submitButtonIsDisabled = false;
  }

  handleSliderChange(e: SliderChangeEvent): void {
    if (e.value === undefined) return;
    this.sliderPercent = e.value
  }

  ngOnDestroy() {
    this.billFormBuilder.reset();
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
