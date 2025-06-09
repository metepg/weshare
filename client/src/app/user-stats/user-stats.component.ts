import { Component, OnInit, signal } from '@angular/core';
import { ChartModule } from 'primeng/chart';
import { CATEGORY_COLORS } from '../../constants/constants';
import { BillService } from '../../services/bill/bill.service';
import { DecimalPipe } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { Bill } from '../../model/Bill';
import { CalculationResult } from '../../model/Stats';
import { TranslationService } from '../../services/translation/translation.service';
import { UserService } from '../../services/user/user.service';
import { BillCategoryCode } from '../../constants/Categories';
import { ChartData, ChartOptions } from 'chart.js';
import { LocalStorageService } from '../../services/local-storage/local-storage.service';
import { User } from '../../model/User';
import { Select } from 'primeng/select';

@Component({
  selector: 'app-user-stats',
  imports: [
    ChartModule,
    DecimalPipe,
    ReactiveFormsModule,
    TranslateModule,
    Select
  ],
  templateUrl: './user-stats.component.html',
  styleUrl: './user-stats.component.scss'
})

export class UserStatsComponent implements OnInit {
  filterForm: FormGroup;
  users: { label: string; value: number }[] = [];
  totalAmount = signal(0);
  chartData: ChartData;
  chartOptions: ChartOptions;
  currentUser: User | null;
  categories: { label: string; value: BillCategoryCode }[];
  chartLabels: string [] = [];
  calculationResult: CalculationResult;
  categoryVisibilityState: boolean[] = [];

  constructor(
    private readonly billService: BillService,
    private readonly fb: FormBuilder,
    private readonly translationService: TranslationService,
    private readonly userService: UserService,
    private readonly localStorageService: LocalStorageService
  ) {
  }

  ngOnInit() {
    this.currentUser = this.localStorageService.getUser();

    this.userService.getUsers().subscribe((users) => {
      this.users = users.map((user) => ({ label: user.name, value: user.id }));
      this.initializeChart();
    });

    this.filterForm = this.fb.group({
      range: [null],
      user: [this.currentUser?.id]
    });
  }

  initializeChart() {
    this.translationService.getTranslatedCategories().subscribe((categories) => {
      this.chartLabels = categories.map((c) => c.label);
      this.categories = categories;
      if (this.currentUser) {
        this.getTotalAmountByUserId(this.currentUser.id);
      }
    })
    this.chartOptions = {
      plugins: {
        legend: {
          labels: {
            color: getComputedStyle(document.documentElement).getPropertyValue('--text-color')
          },
          onClick: (event, legendItem, legend) => {
            if (legendItem.index != null) {
              this.updateTotalAmount(legendItem.index)
              legend.chart.toggleDataVisibility(legendItem.index);
            }
            legend.chart.update();
          },
        }
      }
    };
  }

  getTotalAmountByUserId(id: number) {
    this.billService.getBillsByUserId(id).subscribe((bills) => {
      this.calculationResult = this.calculateTotals(bills);
      this.totalAmount.set(this.calculationResult.totalOwnAmount);
      this.updateChart(this.calculationResult);
    });
  }

  updateChart(result: CalculationResult) {
    if (this.chartLabels.length === 0) return;

    this.chartData = {
      labels: this.chartLabels,
      datasets: [
        {
          data: Object.values(result.categorizedTotals),
          backgroundColor: CATEGORY_COLORS,
          hoverBackgroundColor: CATEGORY_COLORS
        }
      ]
    };
  }

  updateTotalAmount(category: number) {
    const categoryAmount = this.calculationResult.categorizedTotals[category];
    if (this.categoryVisibilityState[category]) {
      this.totalAmount.update((value) => value + categoryAmount);
    } else {
      this.totalAmount.update((value) => value - categoryAmount);
    }
    this.categoryVisibilityState[category] = !this.categoryVisibilityState[category];
  }

  calculateTotals(bills: Bill[]): CalculationResult {
    const initialCategories = CATEGORY_COLORS.reduce((acc, _, index) => {
      acc[index] = 0;
      return acc;
    }, {} as Record<number, number>);

    return bills.reduce((acc: CalculationResult, bill: Bill) => {
      bill.ownAmount = Math.abs(bill.ownAmount);
      acc.totalOwnAmount += bill.ownAmount;

      acc.categorizedTotals[bill.categoryId] += bill.ownAmount;

      return acc;
    }, {totalOwnAmount: 0, categorizedTotals: initialCategories});
  }

  handleOnChange() {
    const rawUser: unknown = this.filterForm.get('user')?.value;
    const user: number | null = typeof rawUser === 'number' ? rawUser : null;
    console.log(user)

    this.categoryVisibilityState = Array.from({ length: this.categories.length }, () => false);

    if (user !== null) {
      this.getTotalAmountByUserId(user);
    }
  }

}
