import { Component, Input, OnInit } from '@angular/core';
import { Bill } from '../../model/Bill';
import { NgClass, DecimalPipe, DatePipe } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';

@Component({
    selector: 'app-bill',
    templateUrl: './bill.component.html',
    styleUrls: ['./bill.component.css'],
    standalone: true,
  imports: [NgClass, DecimalPipe, DatePipe, TranslateModule]
})
export class BillComponent implements OnInit {
  @Input() bill: Bill;
  @Input() username: string;
  amount: number;
  category: number;
  date: Date;
  description: string;
  isPaid: boolean;
  ownAmount: number;
  owner: string;
  userIsOwnerOfBill: boolean;


  ngOnInit(): void {
    this.amount = this.bill.amount
    this.category = this.bill.category
    this.date = this.bill.date
    this.description = this.bill.description
    this.isPaid = this.bill.paid
    this.ownAmount = Math.abs(this.bill.ownAmount);
    this.owner = this.bill.owner;
    this.userIsOwnerOfBill = this.owner === this.username;
  }

}
