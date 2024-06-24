import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { Bill } from '../../model/Bill';
import { DatePipe, DecimalPipe, NgClass } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-bill',
  templateUrl: './bill.component.html',
  styleUrls: ['./bill.component.css'],
  standalone: true,
  imports: [
    DatePipe,
    DecimalPipe,
    TranslateModule,
    NgClass
  ]
})
export class BillComponent implements OnInit, OnChanges {
  @Input() bill: Bill;
  @Input() username: string | null;
  amount: number;
  category: number;
  date: Date;
  description: string;
  isPaid: boolean;
  ownAmount: number;
  owner: string;
  userIsOwnerOfBill: boolean;
  @Output() editBillEmitter = new EventEmitter<Bill>();

  ngOnInit(): void {
    this.assignBillProperties();
  }
  
  ngOnChanges(changes: SimpleChanges) {
    if (changes['bill']) {
      this.assignBillProperties();
    }
  }

  private assignBillProperties(): void {
    this.amount = this.bill.amount;
    this.category = this.bill.category;
    this.date = this.bill.date;
    this.description = this.bill.description;
    this.isPaid = this.bill.paid;
    this.ownAmount = Math.abs(this.bill.ownAmount);
    this.owner = this.bill.owner;
    this.userIsOwnerOfBill = this.owner === this.username;
  }

  editBill() {
    if (this.bill.paid) return;
    this.editBillEmitter.emit(this.bill);
  }
}
