import { Component, OnInit } from '@angular/core';
import { BillService } from '../../services/bill/bill.service';
import { Bill } from '../model/Bill';
import { Observable, tap } from 'rxjs';

@Component({
  selector: 'app-show-statistics',
  templateUrl: './show-statistics.component.html',
  styleUrls: ['./show-statistics.component.css']
})
export class ShowStatisticsComponent implements OnInit {
  yearOptions: Object[];
  selectedYear: number;
  data: any;
  monthlyJoku: Map<number, number>;
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
    this.monthlyJoku = new Map<number, number>();
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
          label: "Bensa",
          backgroundColor: "lightgreen",
          data: data.get("Bensa")
        },
        {
          label: "Ruoka",
          backgroundColor: "pink",
          data: data.get("Ruoka")
        },
        {
          label: "Laskut",
          backgroundColor: "gold",
          data: data.get("Laskut")
        },
        {
          label: "Muut",
          backgroundColor: "skyblue",
          data: data.get("Muut")
        }
      ]
    };

  }

  onChange(event: any) {
    const year = event.value;
    this.getStatistics(year)
  }

  getStatistics(year: number) {
    this.bills$ = this.billService.getBillsByYear(year).pipe(
      tap((bills: Bill[]) => {
        this.monthlyValuesByCategory = new Map<string, number[]>();
        bills.map((bill: Bill) => {
          const month = new Date(bill.date).getMonth();
          const category = bill.category;
          const arr = this.monthlyValuesByCategory.get(category) ?? new Array(12).fill(null);
          arr[month] = bill.amount;
          this.monthlyValuesByCategory.set(category, arr);
        })
        this.setData(this.monthlyValuesByCategory);
      })
    )
  }
}
