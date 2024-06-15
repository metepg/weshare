import { AfterViewChecked, Component, Input, OnInit } from '@angular/core';
import { Bill } from '../../model/Bill';
import { Observable } from 'rxjs';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { BillComponent } from '../bill/bill.component';
import { NgIf, NgFor, AsyncPipe } from '@angular/common';

@Component({
    selector: 'app-show-bills',
    templateUrl: './show-bills.component.html',
    styleUrls: ['./show-bills.component.css'],
    standalone: true,
    imports: [NgIf, NgFor, BillComponent, ProgressSpinnerModule, AsyncPipe]
})
export class ShowBillsComponent implements OnInit, AfterViewChecked {
  @Input() bills$: Observable<Bill[]>;
  @Input() username: string;

  constructor() {
  }

  ngOnInit(): void {
  }

  ngAfterViewChecked(): void {
    window.scrollTo(0, document.body.scrollHeight);
  }


}
