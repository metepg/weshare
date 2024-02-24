import { Component, OnInit } from '@angular/core';
import { BillService } from '../../services/bill/bill.service';
import { Observable, of } from 'rxjs';
import { Bill } from '../../model/Bill';
import { FormBuilder } from '@angular/forms';
import { ConfirmationService, MessageService } from 'primeng/api';
import { UserService } from '../../services/user/user.service';
import Messages from '../../utils/Messages';
import { Constants } from '../../utils/Constants';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css'],
  providers: [MessageService, UserService],
})
export class MainComponent implements OnInit {
  NEW_BILL = Constants.NEW_BILL;
  SHOW_BILLS = Constants.SHOW_BILLS;
  SHOW_STATISTICS = Constants.SHOW_STATISTICS;
  activeTab: number;
  bills$: Observable<Bill[]>;
  username: string;
  isLoading = false;
  debt: number;

  constructor(
    private billService: BillService,
    private messageService: MessageService,
    private userService: UserService,
    private formBuilder: FormBuilder,
    private confirmationService: ConfirmationService
  ) {
  }

  ngOnInit(): void {
    this.loadBills();
    this.showTab(this.SHOW_BILLS);
    this.userService
    .getUsername()
    .subscribe((username: string) => (this.username = username));
  }

  loadBills(): void {
    this.bills$ = this.billService.getBills();
    this.billService.getTotalAmount().subscribe((amount: number): void => {
      this.debt = amount;
    });
  }

  onFormSubmit(formIsValid: boolean): void {
    if (!formIsValid) {
      this.messageService.add(Messages.ERROR.invalidBillForm);
      return;
    }
    this.messageService.add(Messages.SUCCESS.validBillForm);
    this.loadBills();
    this.showTab(this.SHOW_BILLS);
  }

  showTab(tab: number): void {
    this.activeTab = tab;
  }

  payDebt(): void {
    this.confirmationService.confirm({
      header: 'Varmistus',
      message: `Haluatko maksaa velkasi?`,
      accept: (): void => {
        this.isLoading = true;
        const resetBill = new Bill(0, '', '', this.debt, this.username);
        this.billService.payDebt(resetBill).subscribe((response: HttpResponse<Bill[]>) => {
          const status: number = response.status;
          const body: Bill[] | null = response.body;
          if (status === 200 && body) {
            this.isLoading = false;
            this.loadBills();
            this.bills$ = of(body);
            this.messageService.add({
              severity: 'success',
              summary: `Velat nollattu.`,
            });
            this.isLoading = false;
          } else {
            this.messageService.add(Messages.ERROR.unknownError);
          }
        });
      },
      reject: () => {
        this.messageService.add(Messages.ERROR.paymentCancelled);
      },
    });
  }
}
