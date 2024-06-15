import { Component, OnInit } from '@angular/core';
import { BillService } from '../../services/bill/bill.service';
import { Bill } from '../../model/Bill';
import { Observable, tap } from 'rxjs';
import {BillCategoryCode} from '../../utils/Categories';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { ChartModule } from 'primeng/chart';
import { NgIf, AsyncPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/dropdown';

@Component({
    selector: 'app-show-statistics',
    templateUrl: './show-statistics.component.html',
    styleUrls: ['./show-statistics.component.css'],
    standalone: true,
    imports: [DropdownModule, FormsModule, NgIf, ChartModule, ProgressSpinnerModule, AsyncPipe]
})
export class ShowStatisticsComponent implements OnInit {
  yearOptions: Object[];
  selectedYear: number = new Date().getFullYear();
  data: any;
  monthlyValuesByCategory: Map<string, number[]>
  bills$: Observable<Bill[]>;


  StackedOptions = {
    maintainAspectRatio: false,
    plugins: {
      legend: {
        labels: {
          color: "#black"
        }
      }
    },
    scales: {
      x: {
        stacked: true,
        ticks: {
          color: "#black"
        },
        grid: {
          color: "rgba(255,255,255,0.2)"
        }
      },
      y: {
        stacked: true,
        ticks: {
          color: "#black"
        },
        grid: {
          color: "rgba(255,255,255,0.2)"
        }
      }
    }
  };

  constructor(private billService: BillService) {
    const currentYear = new Date().getFullYear();
    this.yearOptions = Array(currentYear - (currentYear - 4))
    .fill('')
    .map((v, idx) => {
      const year = currentYear - idx
      return {name: year.toString(), code: year}
    });
  }

  ngOnInit(): void {
    this.monthlyValuesByCategory = new Map<string, number[]>();
    this.getStatistics(new Date().getFullYear())
    window.scrollTo(0, document.body.scrollHeight);
  }


  setData(data: Map<string, number[]>): void {
    this.data = {
      labels: [
        "Tammikuu",
        "Helmikuu",
        "Maaliskuu",
        "Huhtikuu",
        "Toukokuu",
        "Kesäkuu",
        "Heinäkuu",
        "Elokuu",
        "Syyskuu",
        "Lokakuu",
        "Marraskuu",
        "Joulukuu",
      ],
      datasets: [
        {
          label: BillCategoryCode[0],
          backgroundColor: "lightgreen",
          data: data.get(BillCategoryCode[0])
        },
        {
          label: BillCategoryCode[1],
          backgroundColor: "pink",
          data: data.get(BillCategoryCode[1])
        },
        {
          label: BillCategoryCode[2],
          backgroundColor: "gold",
          data: data.get(BillCategoryCode[2])
        },
        {
          label: BillCategoryCode[3],
          backgroundColor: "red",
          data: data.get(BillCategoryCode[3])
        },
        {
          label: BillCategoryCode[4],
          backgroundColor: "skyblue",
          data: data.get(BillCategoryCode[4])
        },
        {
          label: BillCategoryCode[5],
          backgroundColor: "grey",
          data: data.get(BillCategoryCode[5])
        }
      ]
    };

  }

  onChange(event: any) {
    const year = event.value;
    this.getStatistics(year)
  }

  getStatistics(year: number): void {
    this.bills$ = this.billService.getBillsByYear(year).pipe(
      tap((bills: Bill[]): void => {
        this.monthlyValuesByCategory = new Map<string, number[]>();
        bills.map((bill: Bill): void => {
          const month: number = new Date(bill.date).getMonth();
          const category: string = BillCategoryCode[bill.category];
          const amount: number = bill.amount;
          const arr = this.monthlyValuesByCategory.get(category) ?? new Array(12).fill(null);
          arr[month] += amount;
          this.monthlyValuesByCategory.set(category, arr);
        })
        this.setData(this.monthlyValuesByCategory);
      })
    )
  }
}
