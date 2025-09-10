import { Component, computed, inject, signal } from '@angular/core';
import { ChartModule } from 'primeng/chart';
import { CATEGORY_COLORS } from '../../constants/constants';
import { BillService } from '../../services/bill/bill.service';
import { DecimalPipe } from '@angular/common';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { Bill } from '../../model/Bill';
import { CalculationResult } from '../../model/Stats';
import { TranslationService } from '../../services/translation/translation.service';
import { UserService } from '../../services/user/user.service';
import { ChartData, ChartOptions } from 'chart.js';
import { LocalStorageService } from '../../services/local-storage/local-storage.service';
import { User } from '../../model/User';
import { Select } from 'primeng/select';
import { toSignal } from '@angular/core/rxjs-interop';
import { map } from 'rxjs/operators';

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

export class UserStatsComponent {
  private readonly billService = inject(BillService);
  private readonly fb = inject(FormBuilder);
  private readonly translationService = inject(TranslationService);
  private readonly userService = inject(UserService);
  private readonly localStorageService = inject(LocalStorageService);

  readonly currentUser = signal<User | null>(this.localStorageService.getUser());
  readonly currentUserId = computed(() => this.currentUser()?.id ?? 0);
  readonly categoryVisibilityState = signal<boolean[]>([]);
  readonly users = toSignal(
    this.userService.getUsers().pipe(
      map((users) => users.map((user) => ({ label: user.name, value: user.id })))
    ),
    { initialValue: [] }
  );

  readonly categories = toSignal(this.translationService.getTranslatedCategories(), { initialValue: [] });
  readonly chartLabels = computed(() => this.categories().map((c) => c.label));

  billsByUser = this.billService.getBillsByUserId(this.currentUserId);

  filterForm = this.fb.group({
    range: [null],
    user: [this.currentUserId()]
  });


  chartOptions: ChartOptions = {
    animation: false,
    plugins: {
      legend: {
        labels: {
          color: getComputedStyle(document.documentElement).getPropertyValue('--text-color')
        },
        onClick: (_event, legendItem, legend) => {
          if (legendItem.index != null) {
            this.updateTotalAmount(legendItem.index);
            legend.chart.toggleDataVisibility(legendItem.index);
            legend.chart.update();
          }
        }
      }
    }
  };

  readonly calculationResult = computed(() => {
    const bills = this.billsByUser.value();
    return bills ? this.calculateTotals(bills) : null;
  });

  readonly totalAmount = computed(() => {
    const result = this.calculationResult();
    const visibility = this.categoryVisibilityState();

    if (!result) {
      return 0;
    }

    const excludedCategoryTotalAmount = Object.entries(result.categorizedTotals)
      .reduce((acc, [index, value]) => (visibility[+index] ? acc + value : acc), 0);

    return result.totalOwnAmount - excludedCategoryTotalAmount;
  });

  readonly chartData = computed<ChartData | null>(() => {
    const result = this.calculationResult();
    const labels = this.chartLabels();

    if (!result || labels.length === 0) {
      return null;
    }

    const values = this.chartLabels().map((_, i) => result.categorizedTotals[i] ?? 0);

    return {
      labels: labels,
      datasets: [
        {
          data: values,
          backgroundColor: CATEGORY_COLORS,
          hoverBackgroundColor: CATEGORY_COLORS,
        },
      ],
    };
  });

  updateTotalAmount(categoryIndex: number) {
    this.categoryVisibilityState.update((state) => {
      const copy = state.slice();
      copy[categoryIndex] = !copy[categoryIndex];
      return copy;
    });
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
}
