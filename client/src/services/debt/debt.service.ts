import { Injectable } from '@angular/core';
import { Signal, signal } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DebtService {
  private debtSignal = signal<number>(0);

  get debt(): Signal<number> {
    return this.debtSignal;
  }

  setDebt(amount: number): void {
    this.debtSignal.set(amount);
  }
}
