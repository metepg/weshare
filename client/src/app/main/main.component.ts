import { Component, DoCheck, inject, OnInit } from '@angular/core';
import { BillService } from '../../services/bill/bill.service';
import { Observable, of } from 'rxjs';
import { Bill } from '../../model/Bill';
import { ConfirmationService, MessageService, PrimeTemplate } from 'primeng/api';
import { UserService } from '../../services/user/user.service';
import Messages from '../../constants/Messages';
import { HttpResponse } from '@angular/common/http';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { NavbarComponent } from '../navbar/navbar.component';
import { SplitButtonModule } from 'primeng/splitbutton';
import { SelectButtonModule } from 'primeng/selectbutton';
import { FormsModule } from '@angular/forms';
import { NgStyle } from '@angular/common';
import { Router, RouterOutlet } from '@angular/router';
import { DebtService } from '../../services/debt/debt.service';
import { LocalStorageService } from '../../services/local-storage/local-storage.service';
import { Button } from 'primeng/button';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css'],
  imports: [
    NavbarComponent,
    ConfirmDialogModule,
    PrimeTemplate,
    ProgressSpinnerModule,
    SplitButtonModule,
    SelectButtonModule,
    FormsModule,
    NgStyle,
    Button,
    RouterOutlet
  ]
})
export class MainComponent implements OnInit, DoCheck {
  private readonly billService = inject(BillService);
  private readonly messageService = inject(MessageService);
  private readonly confirmationService = inject(ConfirmationService);
  private readonly router = inject(Router);
  private readonly debtService = inject(DebtService);
  private readonly localStorageService = inject(LocalStorageService);
  private readonly userService = inject(UserService);

  bills$: Observable<Bill[]>;
  isLoading = false;
  debt = this.debtService.debt;
  sidebarVisible = true;
  options: { icon: string; value: string }[] = [
    { icon: 'pi pi-chart-bar', value: 'chart' },
    { icon: 'pi pi-search', value: 'search' },
    { icon: 'pi pi-user', value: 'hof' },
  ];
  option: { icon: string; value: string } | null;

  ngOnInit(): void {
    this.option = this.options[0];
    this.userService.getCurrentUser().subscribe((user) => {
      this.localStorageService.setUser(user);
      this.userService.getTotalDebtAmount(user.id).subscribe((amount: number): void => {
        this.debtService.setDebt(amount);
      });
    });
  }

  ngDoCheck(): void {
    if (this.option?.value === 'search') {
      this.sidebarVisible = true;
    }
  }

  /**
   * Shows a confirmation dialog, creates a reset bill, and updates the state.
   */
  payDebt(): void {
    this.confirmationService.confirm({
      header: 'Varmistus',
      message: `Haluatko maksaa velkasi?`,
      accept: (): void => {
        const user = this.localStorageService.getUser();

        if (user == null) {
          return;
        }

        this.isLoading = true;
        const resetBill = new Bill(0, 7, '', this.debt(), user.id, user.name);
        this.billService.payDebt(resetBill).subscribe((response: HttpResponse<Bill[]>) => {
          const body: Bill[] | null = response.body;
          if (response.ok && body) {
            this.isLoading = false;
            this.debtService.setDebt(0);
            this.bills$ = of(body);
            this.messageService.add({severity: 'success', summary: `Velat nollattu.`,});
            void this.router.navigate(['bills'])
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
