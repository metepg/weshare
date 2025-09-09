import { Component, computed, inject, OnInit, signal } from '@angular/core';
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
  private readonly billService = inject(BillService);
  private readonly fb = inject(FormBuilder);
  private readonly translationService = inject(TranslationService);
  private readonly userService = inject(UserService);
  private readonly localStorageService = inject(LocalStorageService);

  currentUser = signal<User | null>(this.localStorageService.getUser());
  currentUserId = computed(() => this.currentUser()?.id ?? 0);
  categoryVisibilityState = signal<boolean[]>([]);
  billsByUser = this.billService.getBillsByUserId(this.currentUserId);

  users: { label: string; value: number }[] = [];
  categories: { label: string; value: BillCategoryCode }[] = [];
  chartLabels: string[] = [];
  filterForm!: FormGroup;

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

  calculationResult = computed(() => {
    const bills = this.billsByUser.value();
    return bills ? this.calculateTotals(bills) : null;
  });

  totalAmount = computed(() => {
    const result = this.calculationResult();
    const visibility = this.categoryVisibilityState();

    if (!result) {
      return 0;
    }

    const excludedCategoryTotalAmount = Object.entries(result.categorizedTotals)
      .reduce((acc, [index, value]) => (visibility[+index] ? acc + value : acc), 0);

    return result.totalOwnAmount - excludedCategoryTotalAmount;
  });

  chartData = computed<ChartData | null>(() => {
    const result = this.calculationResult();

    if (!result || this.chartLabels.length === 0) {
      return null;
    }

    const values = this.chartLabels.map((_, i) => result.categorizedTotals[i] ?? 0);

    return {
      labels: this.chartLabels,
      datasets: [
        {
          data: values,
          backgroundColor: CATEGORY_COLORS,
          hoverBackgroundColor: CATEGORY_COLORS,
        },
      ],
    };
  });

  ngOnInit() {
    this.filterForm = this.fb.group({
      range: [null],
      user: [this.currentUserId()]
    });

    this.userService.getUsers().subscribe((users) => {
      this.users = users.map((user) => ({ label: user.name, value: user.id }));
    });

    this.translationService.getTranslatedCategories().subscribe((categories) => {
      this.categories = categories;
      this.chartLabels = categories.map((c) => c.label);
      this.categoryVisibilityState.set(new Array<boolean>(categories.length).fill(false));
    });

    this.filterForm.get('user')!.valueChanges.subscribe((id: number | null) => {
      if (typeof id === 'number') {
        this.currentUser.set({ ...this.currentUser()!, id });
        this.categoryVisibilityState.set(new Array<boolean>(this.categories.length).fill(false));
      }
    });
  }

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
