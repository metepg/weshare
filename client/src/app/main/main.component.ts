import { Component, DoCheck, OnInit, signal } from '@angular/core';
import { BillService } from '../../services/bill/bill.service';
import { Observable, of } from 'rxjs';
import { Bill } from '../../model/Bill';
import {
  ConfirmationService,
  MessageService,
  PrimeTemplate
} from 'primeng/api';
import { PersonService } from '../../services/person/person.service';
import Messages from '../../constants/Messages';
import { View } from '../../constants/View';
import { HttpResponse } from '@angular/common/http';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { ButtonDirective } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ShowChartComponent } from '../show-chart/show-chart.component';
import { ShowBillsComponent } from '../show-bills/show-bills.component';
import { NewBillFormComponent } from '../new-bill-form/new-bill-form.component';
import { NavbarComponent } from '../navbar/navbar.component';
import { SearchBillsComponent } from '../search-bills/search-bills.component';
import { SplitButtonModule } from 'primeng/splitbutton';
import { SelectButtonModule } from 'primeng/selectbutton';
import { FormsModule } from '@angular/forms';
import { UserStatsComponent } from '../user-stats/user-stats.component';
import { TranslateModule } from '@ngx-translate/core';
import { NgStyle } from '@angular/common';
import { Router } from '@angular/router';

@Component({
    selector: 'app-main',
    templateUrl: './main.component.html',
    styleUrls: ['./main.component.css'],
    providers: [MessageService, PersonService],
    standalone: true,
  imports: [
    NavbarComponent,
    NewBillFormComponent,
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
  username: string;
  isLoading = false;
  debt = signal<number>(0);
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
    private userService: PersonService,
    private confirmationService: ConfirmationService,
    private router: Router
  ) {
  }
  
  ngOnInit(): void {
    this.option = this.options[0];
    const username = localStorage.getItem('name');
    if (!username) {
      this.userService.getUsername().subscribe((username: string) => {
        localStorage.setItem('name', username);
        this.username = username;
      });
    }
    this.getDebtAmount();
    
    this.billService.billCreated$.subscribe((amount) => {
      this.debt.update(previousDebt => previousDebt + amount);
    });
  }

  ngDoCheck(): void {
    if (this.option?.value === 'search') {
      this.sidebarVisible = true;
    }
  }
  
  getDebtAmount(): void {
    this.billService.getTotalAmount().subscribe((amount: number): void => {
      this.debt.set(amount);
    });
  }

  payDebt(): void {
    this.confirmationService.confirm({
      header: 'Varmistus',
      message: `Haluatko maksaa velkasi?`,
      accept: (): void => {
        this.isLoading = true;
        const resetBill = new Bill(0, -1, '', this.debt(), this.username);
        this.billService.payDebt(resetBill).subscribe((response: HttpResponse<Bill[]>) => {
          const body: Bill[] | null = response.body;
          if (response.ok && body) {
            this.isLoading = false;
            this.getDebtAmount();
            this.bills$ = of(body);
            this.messageService.add({
              severity: 'success',
              summary: `Velat nollattu.`,
            });
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
