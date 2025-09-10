import { Component, inject, input, model, OnDestroy, OnInit, output } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { InputTextModule } from 'primeng/inputtext';
import { SliderChangeEvent, SliderModule } from 'primeng/slider';
import { DecimalPipe } from '@angular/common';
import { Button } from 'primeng/button';
import { Bill } from '../../model/Bill';
import { BillCategoryCode } from '../../constants/Categories';
import { isValidCategory, isValidDescription } from '../../utils/formValidationUtils';
import { TranslationService } from '../../services/translation/translation.service';
import { Subscription } from 'rxjs';
import { ConfirmationService } from 'primeng/api';
import { User } from '../../model/User';
import { LocalStorageService } from '../../services/local-storage/local-storage.service';
import { IconField } from 'primeng/iconfield';
import { InputIcon } from 'primeng/inputicon';
import { Select } from 'primeng/select';

@Component({
  selector: 'app-bill-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    TranslateModule,
    InputTextModule,
    SliderModule,
    DecimalPipe,
    Button,
    IconField,
    InputIcon,
    Select
  ],
  templateUrl: './bill-form.component.html',
  styleUrl: './bill-form.component.scss'
})

export class BillFormComponent implements OnInit, OnDestroy {
  private readonly formBuilder = inject(FormBuilder);
  private readonly translationService = inject(TranslationService);
  private readonly confirmationService = inject(ConfirmationService);
  private readonly localStorageService = inject(LocalStorageService);

  private subscription: Subscription;
  categories: { label: string; value: BillCategoryCode }[] = [];
  submitButtonIsDisabled = true;
  user: User | null;
  readonly disabledFields = input<string[]>([]);
  readonly id = input<number>();
  readonly showDeleteBillButton = input(false);

  readonly description = model<string>();
  readonly ownShareOfBill = model<number>();
  readonly sliderPercent = model(50);
  readonly amount = model<number>();
  readonly category = model<number>();
  readonly paid = model(false);

  formEmitter = output<Bill>();
  deleteBillEmitter = output<number>();
  billFormBuilder: FormGroup<{
    amount: FormControl<number | null>;
    category: FormControl<number | null>;
    description: FormControl<string | null>;
    sliderPercent: FormControl<number | null>;
  }>

  ngOnInit(): void {
    this.billFormBuilder = this.formBuilder.group({
      amount: [
        { value: this.amount() ?? null, disabled: this.disabledFields().includes('amount') },
        [Validators.required, Validators.min(1)]
      ],
      category: [
        { value: this.category() ?? null, disabled: this.disabledFields().includes('category') },
        [Validators.required, isValidCategory]
      ],
      description: [
        { value: this.description() ?? null, disabled: this.disabledFields().includes('description') },
        [Validators.required, isValidDescription]
      ],
      sliderPercent: {
        value: this.sliderPercent(),
        disabled: this.disabledFields().includes('sliderPercent')
      }
    });

    this.billFormBuilder.valueChanges.subscribe((value) => {
      if (!value.amount) return
      this.ownShareOfBill.set(Math.round((value.amount * (this.sliderPercent() / 100)) * 100) / 100);
    })

    this.subscription = this.translationService.getTranslatedCategories().subscribe((translatedCategories) => {
      this.categories = translatedCategories;
    });
  }

  isControlInvalid(controlName: string): boolean {
    const control = this.billFormBuilder.get(controlName);
    return control ? control.invalid && control.touched : false;
  }

  onSubmit(): void {
    this.submitButtonIsDisabled = true;
    const {amount, category, description} = this.billFormBuilder.value;
    this.user = this.localStorageService.getUser();

    if (!this.billFormBuilder.valid || !this.user) {
      return;
    }

    this.formEmitter.emit(new Bill(amount!, category!, description!, this.ownShareOfBill()!, this.user.id, this.user.name, this.paid()));
    this.submitButtonIsDisabled = false;
  }

  handleSliderChange(e: SliderChangeEvent): void {
    if (e.value === undefined) return;
    this.sliderPercent.set(e.value);
  }

  deleteBill() {
    const billId = this.id();
    if (!billId) {
      return;
    }

    this.confirmationService.confirm({
      header: 'Varmistus', message: `Haluatko varmasti poistaa laskun?`,
      accept: (): void => {
        this.deleteBillEmitter.emit(billId);
      },

      reject: () => {},
    });
  }

  ngOnDestroy() {
    this.billFormBuilder.reset();
    this.subscription.unsubscribe();
  }

}
