import { Component, Input, AfterViewChecked } from '@angular/core';
import { Bill } from '../../model/Bill';
import { Observable } from 'rxjs';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { BillComponent } from '../bill/bill.component';
import { AsyncPipe, NgFor, NgIf } from '@angular/common';

@Component({
    selector: 'app-show-bills',
    templateUrl: './show-bills.component.html',
    styleUrls: ['./show-bills.component.css'],
    standalone: true,
    imports: [NgIf, NgFor, BillComponent, ProgressSpinnerModule, AsyncPipe]
})
export class ShowBillsComponent implements AfterViewChecked {
  @Input() bills$: Observable<Bill[]>;
  @Input() username: string;

  ngAfterViewChecked(): void {
    window.scrollTo(0, document.body.scrollHeight);
  }

}
