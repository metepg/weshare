import { Component, OnInit, signal } from '@angular/core';
import { ChartModule } from 'primeng/chart';
import { BillCategoryCode } from '../../utils/Categories';
import { CATEGORY_COLORS } from '../../utils/constants';
import { BillService } from '../../services/bill/bill.service';
import { DecimalPipe } from '@angular/common';
import { CalendarModule } from 'primeng/calendar';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/dropdown';
import { DividerModule } from 'primeng/divider';

@Component({
  selector: 'app-user-stats',
  standalone: true,
  imports: [
    ChartModule,
    DecimalPipe,
    CalendarModule,
    DropdownModule,
    ReactiveFormsModule,
    DividerModule
  ],
  templateUrl: './user-stats.component.html',
  styleUrl: './user-stats.component.scss'
})
export class UserStatsComponent implements OnInit {
  filterForm: FormGroup;
  users = [
    { label: 'User1', value: 'user1' },
    { label: 'User2', value: 'user2' }
  ];
  totalAmount = signal(0);
  data: unknown;
  options: unknown;
  defaultUser = localStorage.getItem('name');
  
  constructor(private billService: BillService, private fb: FormBuilder) {
  }

  ngOnInit() {
    console.log(this.defaultUser);
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    this.billService.getTotalAmount().subscribe(total => this.totalAmount.set(total));

    this.data = {
      labels: Object.keys(BillCategoryCode).filter(key => isNaN(Number(key))),
      datasets: [
        {
          data: [540, 325, 702, 232,434,232],
          backgroundColor: CATEGORY_COLORS,
          hoverBackgroundColor: CATEGORY_COLORS.map(color => this.adjustHoverColor(color))
        }
      ]
    };

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
      user: [this.defaultUser]
    });
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
