import { Component, input, output } from '@angular/core';
import { Bill } from '../../model/Bill';
import { DatePipe, DecimalPipe, NgClass } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { User } from '../../model/User';

@Component({
  selector: 'app-bill',
  templateUrl: './bill.component.html',
  styleUrls: ['./bill.component.css'],
  imports: [
    DatePipe,
    DecimalPipe,
    TranslateModule,
    NgClass
  ]
})
export class BillComponent {
  user = input<User>();
  bill = input<Bill>();
  editBillEmitter = output<Bill>();

  get isDebtPaidBox() {
    // Category 7 is the debtPaid box
    return this.bill()?.categoryId === 7;
  }

  get formattedDate() {
    return this.bill()?.date;
  }

  get ownerText() {
    return this.isUserOwnerOfBill
      ? 'Maksoit velat'
      : `${this.bill()?.ownerName} maksoi velat`;
  }

  get ownShareText() {
    return this.isUserOwnerOfBill
      ? 'Oma'
      : `${this.bill()?.ownerName}n`;
  }

  get ownAmount() {
    return Math.abs(this.bill()?.ownAmount ?? 0);
  }

  get isBillPaid() {
    return this.bill()?.paid;
  }

  get isUserOwnerOfBill() {
    return this.bill()?.ownerId === this.user()?.id;
  }

  editBill() {
    const bill = this.bill();
    if (!bill || bill.paid) {
      return;
    }
    this.editBillEmitter.emit(bill);
  }
}
