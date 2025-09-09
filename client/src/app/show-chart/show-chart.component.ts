import { Component, computed, inject, model, signal } from '@angular/core';
import { BillService } from '../../services/bill/bill.service';
import { ChartData } from 'chart.js';
import { Bill } from '../../model/Bill';
import { BAR_CHART_OPTIONS } from '../../constants/constants';
import { BillCategoryCode } from '../../constants/Categories';
import { FormsModule } from '@angular/forms';
import { ChartModule } from 'primeng/chart';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { generateChartData, generateYearOptions } from '../../utils/chartUtils';
import { TranslateModule } from '@ngx-translate/core';
import { Select } from 'primeng/select';

@Component({
  selector: 'app-show-chart',
  templateUrl: './show-chart.component.html',
  imports: [
    FormsModule,
    ChartModule,
    ProgressSpinnerModule,
    TranslateModule,
    Select
  ],
  styleUrls: ['./show-chart.component.css']
})
export class ShowChartComponent {
  private readonly billService = inject(BillService);

  yearOptions = signal(generateYearOptions(5));
  selectedYear = signal<number>(new Date().getFullYear());
  billsByYear = this.billService.getBillsByYear(this.selectedYear);
  showSideBar = model<boolean>(false);

  protected readonly CHART_OPTIONS = BAR_CHART_OPTIONS;

  readonly monthlyValues = computed(() => {
    const bills = this.billsByYear.value();
    return bills
      ? this.calculateMonthlyAmounts(bills)
      : null;
  });

  readonly data = computed<ChartData | null>(() => {
    const monthlyValues = this.monthlyValues();
    return monthlyValues
      ? generateChartData(monthlyValues, false)
      : null;
  });

  onYearChange(year: number) {
    this.selectedYear.set(year);
  }

  /**
 * Aggregates monthly values by category from a list of bills.
 *
 * @param bills - The list of bills to process.
 * @returns A map where each key is a category and the value is an array of monthly amounts.
 *
 * Example:
 * ```typescript
 * {
 *   "Category1": [
 *     494.35, // January
 *     298.83, // February
 *     265.46, // March
 *     445.57,
 *     155.81,
 *     82.14,
 *     2,
 *     500,
 *     42,
 *     592.55,
 *     212.3,
 *     12.32
 *   ], etc..
 * }
 * ```
 */
  private calculateMonthlyAmounts(bills: Bill[]): Map<string, number[]> {
    const monthlyValuesByCategory = new Map<string, number[]>();
    const removedCategory = bills.filter((b) => b.categoryId !== 7);

    for (const bill of removedCategory) {
      const month = new Date(bill.date).getMonth();
      const category = BillCategoryCode[bill.categoryId];
      // Exclude SettlementBillCategory
      if (category !== BillCategoryCode[BillCategoryCode.Category7]) {
        const amount = bill.amount;
        this.updateMonthlyValues(monthlyValuesByCategory, month, category, amount);
      }
    }

    return monthlyValuesByCategory;
  }

  /**
   * Updates the monthly values for a given category.
   *
   * @param valuesByCategory - Values by category.
   * @param month - The month index to update.
   * @param category - The category of the bill.
   * @param amount - The amount to add to the specified month and category.
   */
  private updateMonthlyValues(valuesByCategory: Map<string, number[]>, month: number, category: string, amount: number): void {
    let arr = valuesByCategory.get(category);
    if (!Array.isArray(arr)) {
      arr = new Array(12).fill(0);
    }
    arr[month] += amount;
    valuesByCategory.set(category, arr);
  }
}
