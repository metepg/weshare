import { Component, OnInit } from '@angular/core';
import { BillService } from '../../services/bill/bill.service';
import { Bill } from '../../model/Bill';
import { Observable, tap } from 'rxjs';
import { BillCategoryCode } from '../../utils/Categories';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { ChartModule } from 'primeng/chart';
import { AsyncPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DropdownChangeEvent, DropdownModule } from 'primeng/dropdown';
import { CATEGORY_COLORS, MONTHS } from '../../utils/constants';

interface ChartData {
  labels: string[];
  datasets: {
    label: string;
    backgroundColor: string;
    data: number[];
  }[];
}

@Component({
  selector: 'app-show-statistics',
  templateUrl: './show-statistics.component.html',
  styleUrls: ['./show-statistics.component.css'],
  standalone: true,
  imports: [DropdownModule, FormsModule, ChartModule, ProgressSpinnerModule, AsyncPipe]
})

export class ShowStatisticsComponent implements OnInit {
  yearOptions: { name: string, code: number }[];
  selectedYear: number = new Date().getFullYear();
  data: ChartData;
  monthlyValuesByCategory: Map<string, number[]>;
  bills$: Observable<Bill[]>;

  StackedOptions = {
    maintainAspectRatio: false,
    plugins: {
      legend: {
        labels: {
          color: "#000000"
        }
      }
    },
    scales: {
      x: {
        stacked: true,
        ticks: {
          color: "#000000"
        },
        grid: {
          color: "rgba(255,255,255,0.2)"
        }
      },
      y: {
        stacked: true,
        ticks: {
          color: "#000000"
        },
        grid: {
          color: "rgba(255,255,255,0.2)"
        }
      }
    }
  };

  constructor(private billService: BillService) {
    const currentYear = new Date().getFullYear();
    this.yearOptions = Array.from({length: 5}, (_, idx) => {
      const year = currentYear - idx;
      return {name: year.toString(), code: year};
    });
  }

  ngOnInit(): void {
    this.monthlyValuesByCategory = new Map<string, number[]>();
    this.getStatistics(new Date().getFullYear());
    window.scrollTo(0, document.body.scrollHeight);
  }

  setData(data: Map<string, number[]>): void {
    this.data = {
      labels: MONTHS,
      datasets: Object.values(BillCategoryCode)
      .filter(value => typeof value === 'string') // Filter out numeric enum values
      .map((category, index) => ({
        label: category as string,
        backgroundColor: this.getCategoryColor(index),
        data: data.get(category as string) || new Array(12).fill(0) as number[],
      })),
    };
  }

  getCategoryColor(index: number): string {
    return CATEGORY_COLORS[index] || "grey";
  }

  onChange(event: DropdownChangeEvent): void {
    const year = event.value;
    this.getStatistics(year);
  }

  getStatistics(year: number): void {
    this.bills$ = this.billService.getBillsByYear(year).pipe(
      tap((bills: Bill[]): void => {
        this.monthlyValuesByCategory = new Map<string, number[]>();
        bills.forEach((bill: Bill): void => {
          const month: number = new Date(bill.date).getMonth();
          const category: string = BillCategoryCode[bill.category];
          const amount: number = bill.amount;
          const arr = this.monthlyValuesByCategory.get(category) ?? new Array(MONTHS.length).fill(0);
          arr[month] += amount;
          this.monthlyValuesByCategory.set(category, arr);
        });
        this.setData(this.monthlyValuesByCategory);
      })
    );
  }
}
