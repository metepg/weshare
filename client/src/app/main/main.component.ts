import { Component, OnInit } from '@angular/core';
import { BillService } from '../../services/bill/bill.service';
import { Observable, of } from 'rxjs';
import { Bill } from '../../model/Bill';
import { ConfirmationService, MessageService, PrimeTemplate } from 'primeng/api';
import { PersonService } from '../../services/person/person.service';
import Messages from '../../utils/Messages';
import { View } from '../../utils/View';
import { HttpResponse } from '@angular/common/http';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { ButtonDirective } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ShowStatisticsComponent } from '../show-statistics/show-statistics.component';
import { ShowBillsComponent } from '../show-bills/show-bills.component';
import { NewBillFormComponent } from '../new-bill-form/new-bill-form.component';
import { NavbarComponent } from '../navbar/navbar.component';
import { SearchBillsComponent } from '../search-bills/search-bills.component';
import { SplitButtonModule } from 'primeng/splitbutton';
import { SelectButtonModule } from 'primeng/selectbutton';
import { FormsModule } from '@angular/forms';

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
    ShowStatisticsComponent,
    ConfirmDialogModule,
    PrimeTemplate,
    ButtonDirective,
    ProgressSpinnerModule,
    SearchBillsComponent,
    SplitButtonModule,
    SelectButtonModule,
    FormsModule
  ],
})
export class MainComponent implements OnInit {
  protected readonly View = View;
  activeTab: number;
  bills$: Observable<Bill[]>;
  username: string;
  isLoading = false;
  debt: number;
  showSideBar = false;
  options: {icon: string, value: string}[] = [
    { icon: 'pi pi-chart-bar', value: 'chart' },
    { icon: 'pi pi-search', value: 'search' },
  ];
  option = this.options[0];
  
  constructor(
    private billService: BillService,
    private messageService: MessageService,
    private userService: PersonService,
    private confirmationService: ConfirmationService
  ) {
  }

  ngOnInit(): void {
    this.loadBills();
    this.showTab(View.SHOW_BILLS);
    this.userService.getUsername().subscribe((username: string) => (this.username = username));
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
    this.showTab(View.SHOW_BILLS);
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
        const resetBill = new Bill(0, -1, '', this.debt, this.username);
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
