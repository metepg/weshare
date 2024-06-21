import {
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
  signal
} from '@angular/core';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { BillService } from '../../services/bill/bill.service';
import { ChartData } from 'chart.js';
import { Bill } from '../../model/Bill';
import { BAR_CHART_OPTIONS, } from '../../constants/constants';
import { BillCategoryCode } from '../../constants/Categories';
import { DropdownChangeEvent, DropdownModule } from 'primeng/dropdown';
import { FormsModule } from '@angular/forms';
import { ChartModule } from 'primeng/chart';
import { AsyncPipe } from '@angular/common';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { generateChartData, generateYearOptions } from '../../utils/chartUtils';

@Component({
  selector: 'app-show-statistics',
  templateUrl: './show-statistics.component.html',
  standalone: true,
  imports: [
    DropdownModule,
    FormsModule,
    ChartModule,
    AsyncPipe,
    ProgressSpinnerModule
  ],
  styleUrls: ['./show-statistics.component.css']
})
export class ShowStatisticsComponent implements OnInit {
  protected readonly STACKED_OPTIONS = BAR_CHART_OPTIONS;
  yearOptions = signal<{ name: string, code: number }[]>([]);
  selectedYear = signal<number>(new Date().getFullYear());
  data = signal<ChartData | null>(null);
  monthlyValuesByCategory = signal<Map<string, number[]>>(new Map());
  bills$!: Observable<Bill[]>;
  @Input() showSideBar = signal<boolean>(false);
  @Output() showSideBarChange = new EventEmitter<boolean>();


  constructor(private billService: BillService) {
  }

  onChange(event: DropdownChangeEvent): void {
    this.getStatistics(event.value);
  }

  ngOnInit(): void {
    this.yearOptions.set(generateYearOptions(5));
    this.getStatistics(this.selectedYear());
  }

  /**
   * Fetches bills for a given year, processes them to aggregate monthly values by category,
   * and updates the component's state with the new data.
   *
   * @param year - The year for which to fetch and process bill data.
   */
  getStatistics(year: number): void {
    this.bills$ = this.billService.getBillsByYear(year).pipe(
      tap((bills: Bill[]) => {
        const monthlyValuesByCategory = this.aggregateMonthlyValues(bills);
        this.monthlyValuesByCategory.set(new Map(monthlyValuesByCategory));
        this.data.set(generateChartData(monthlyValuesByCategory))
      })
    )
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
  private aggregateMonthlyValues(bills: Bill[]): Map<string, number[]> {
    const monthlyValuesByCategory = new Map<string, number[]>();

    for (const bill of bills) {
      const month = new Date(bill.date).getMonth();
      const category = BillCategoryCode[bill.category];
      const amount = bill.amount;
      this.updateMonthlyValues(monthlyValuesByCategory, month, category, amount);
    }
    console.log(monthlyValuesByCategory)

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
    const arr = valuesByCategory.get(category) ?? new Array(12).fill(0);
    arr[month] += amount;
    valuesByCategory.set(category, arr);
  }
}
