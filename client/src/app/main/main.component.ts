import { Component, DoCheck, OnInit } from '@angular/core';
import { BillService } from '../../services/bill/bill.service';
import { Observable, of } from 'rxjs';
import { Bill } from '../../model/Bill';
import { ConfirmationService, MessageService, PrimeTemplate } from 'primeng/api';
import { UserService } from '../../services/user/user.service';
import Messages from '../../constants/Messages';
import { View } from '../../constants/View';
import { HttpResponse } from '@angular/common/http';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { ButtonDirective } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ShowChartComponent } from '../show-chart/show-chart.component';
import { ShowBillsComponent } from '../show-bills/show-bills.component';
import { NewBillComponent } from '../new-bill/new-bill.component';
import { NavbarComponent } from '../navbar/navbar.component';
import { SearchBillsComponent } from '../search-bills/search-bills.component';
import { SplitButtonModule } from 'primeng/splitbutton';
import { SelectButtonModule } from 'primeng/selectbutton';
import { FormsModule } from '@angular/forms';
import { UserStatsComponent } from '../user-stats/user-stats.component';
import { TranslateModule } from '@ngx-translate/core';
import { NgStyle } from '@angular/common';
import { Router } from '@angular/router';
import { DebtService } from '../../services/debt/debt.service';
import { LocalStorageService } from '../../services/local-storage/local-storage.service';

@Component({
    selector: 'app-main',
    templateUrl: './main.component.html',
    styleUrls: ['./main.component.css'],
    providers: [MessageService, UserService],
    standalone: true,
  imports: [
    NavbarComponent,
    NewBillComponent,
    ShowBillsComponent,
    ShowChartComponent,
    ConfirmDialogModule,
    PrimeTemplate,
    ButtonDirective,
    ProgressSpinnerModule,
    SearchBillsComponent,
    SplitButtonModule,
    SelectButtonModule,
    FormsModule,
    UserStatsComponent,
    TranslateModule,
    NgStyle
  ],
})
export class MainComponent implements OnInit, DoCheck {
  protected readonly View = View;
  bills$: Observable<Bill[]>;
  isLoading = false;
  debt = this.debtService.debt;
  sidebarVisible = true;
  options: {icon: string, value: string}[] = [
    { icon: 'pi pi-chart-bar', value: 'chart' },
    { icon: 'pi pi-search', value: 'search' },
    { icon: 'pi pi-user', value: 'hof' },
  ];
  option: {icon: string, value: string} | null;
  
  constructor(
    private billService: BillService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private router: Router,
    private debtService: DebtService,
    private localStorageService: LocalStorageService,
    private userService: UserService,
  ) {
  }
  
  ngOnInit(): void {
    this.option = this.options[0];
    this.userService.getCurrentUser().subscribe(user => {
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
            this.router.navigate(['bills'])
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
