import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { Bill } from '../../model/Bill';
import { NgClass, DecimalPipe, DatePipe } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { DialogModule } from 'primeng/dialog';
import { Button } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { ReactiveFormsModule } from '@angular/forms';
import { BillFormComponent } from '../bill-form/bill-form.component';

@Component({
    selector: 'app-bill',
    templateUrl: './bill.component.html',
    styleUrls: ['./bill.component.css'],
    standalone: true,
  imports: [NgClass, DecimalPipe, DatePipe, TranslateModule, DialogModule, Button, InputTextModule, ReactiveFormsModule, BillFormComponent]
})
export class BillComponent implements OnInit, OnChanges {
  @Input() bill: Bill;
  @Input() username: string;
  amount: number;
  category: number;
  date: Date;
  description: string;
  isPaid: boolean;
  ownAmount: number;
  owner: string;
  showDialog = false;
  userIsOwnerOfBill: boolean;


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
    this.showDialog = true;
  }
}
