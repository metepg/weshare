import { Component, input, output } from '@angular/core';
import { Bill } from '../../model/Bill';
import { DatePipe, DecimalPipe, NgClass } from '@angular/common';
import { User } from '../../model/User';
import { TranslatePipe } from '@ngx-translate/core';

@Component({
  selector: 'app-bill',
  templateUrl: './bill.component.html',
  styleUrls: ['./bill.component.css'],
  imports: [
    DatePipe,
    DecimalPipe,
    TranslatePipe,
    NgClass
  ]
})
export class BillComponent {
  readonly user = input.required<User>();
  readonly bill = input.required<Bill>();
  editBillEmitter = output<Bill>();

  get isDebtPaidBox() {
    // Category 7 is the debtPaid box
    return this.bill().categoryId === 7;
  }

  get formattedDate() {
    return this.bill().date;
  }

  get ownerText() {
    return this.isUserOwnerOfBill
      ? 'Maksoit velat'
      : `${this.bill().ownerName} maksoi velat`;
  }

  get ownShareText() {
    return this.isUserOwnerOfBill
      ? 'Oma'
      : `${this.bill().ownerName}n`;
  }

  get ownAmount() {
    return Math.abs(this.bill().ownAmount);
  }

  get isBillPaid() {
    return this.bill().paid;
  }

  get isUserOwnerOfBill() {
    return this.bill().ownerId === this.user().id;
  }

  editBill() {
    const bill = this.bill();
    if (bill.paid) {
      return;
    }
    this.editBillEmitter.emit(bill);
  }
}
