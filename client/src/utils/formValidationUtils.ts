import { AbstractControl, ValidationErrors } from '@angular/forms';
import { BillCategoryCode } from '../constants/Categories';

export function isValidCategory(control: AbstractControl): ValidationErrors | null {
  const value: unknown = control.value;
  if (typeof value !== 'string') {
    return { invalidCategory: true };
  }
  const validValues = Object.values(BillCategoryCode) as string[];
  return validValues.includes(value) ? null : { invalidCategory: true };
}

export function isValidDescription(control: AbstractControl): ValidationErrors | null {
  const value: unknown = control.value;
  return typeof value === 'string' && isNonEmptyString(value) && value.length > 2
    ? null
    : { invalidDescription: true };
}

function isNonEmptyString(value: unknown): boolean {
  return typeof value === 'string' && value.trim() !== '';
}
