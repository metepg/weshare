import { Component, OnInit, signal } from '@angular/core';
import { ChartModule } from 'primeng/chart';
import { BillCategoryCode } from '../../constants/Categories';
import { CATEGORY_COLORS } from '../../constants/constants';
import { BillService } from '../../services/bill/bill.service';
import { DecimalPipe } from '@angular/common';
import { CalendarModule } from 'primeng/calendar';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/dropdown';
import { DividerModule } from 'primeng/divider';
import { TranslateModule } from '@ngx-translate/core';
import { Bill } from '../../model/Bill';
import { CalculationResult } from '../../model/Stats';
import { TranslationService } from '../../services/translation/translation.service';

@Component({
  selector: 'app-user-stats',
  standalone: true,
  imports: [
    ChartModule,
    DecimalPipe,
    CalendarModule,
    DropdownModule,
    ReactiveFormsModule,
    DividerModule,
    TranslateModule
  ],
  templateUrl: './user-stats.component.html',
  styleUrl: './user-stats.component.scss'
})

export class UserStatsComponent implements OnInit {
  filterForm: FormGroup;
  users: {label: string, value: string}[] = [];
  totalAmount = signal(0);
  data: unknown;
  result: CalculationResult;
  options: unknown;
  username = localStorage.getItem('name') || 'user';
  defaultUser: {label: string, value: string};
  
  constructor(
    private billService: BillService,
    private fb: FormBuilder,
    private translationService: TranslationService) {
  }

  ngOnInit() {
    this.defaultUser = {label: this.username, value: this.username}
    this.users.push(this.defaultUser);
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    this.initializeChart()

    this.options = {
      plugins: {
        legend: {
          labels: {
            usePointStyle: true,
            color: textColor
          }
        }
      }
    };
    
    this.filterForm = this.fb.group({
      range: [null],
      user: [this.defaultUser.value]
    });
  }

  initializeChart() {
    if (this.defaultUser) {
      this.billService.getTotalAmountByUserName(this.defaultUser.value).subscribe(bills => {
        this.result = this.calculateTotals(bills);
        this.totalAmount.set(this.result.totalOwnAmount);

        this.translationService.getTranslatedCategories().subscribe(categories => {
          const labels = categories.map(c => c.label);
          this.data = {
            labels: labels,
            datasets: [
              {
                data: Object.values(this.result.categorizedTotals),
                backgroundColor: CATEGORY_COLORS,
                hoverBackgroundColor: CATEGORY_COLORS.map(color => this.adjustHoverColor(color))
              }
            ]
          };
        })
      })
    }
  }

  calculateTotals(bills: Bill[]): CalculationResult {
    return bills.reduce((acc: CalculationResult, bill: Bill) => {
      acc.totalOwnAmount += bill.ownAmount;

      if (bill.category >= 0 && bill.category <= 5) {
        if (!acc.categorizedTotals[bill.category]) {
          acc.categorizedTotals[bill.category] = 0;
        }
        acc.categorizedTotals[bill.category] += bill.ownAmount;
      }

      return acc;
    }, { totalOwnAmount: 0, categorizedTotals: {} });
  }
  
  adjustHoverColor(color: string): string {
    return color;
  }
  
  onSubmit() {
    const range = (this.filterForm?.get('range')?.value || []).filter((date: Date | null) => date !== null);
    const user = this.filterForm.get('user')?.value;

    console.log(range, user);
  }

}
