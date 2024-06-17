import { AfterViewChecked, Component, Input } from '@angular/core';
import { Bill } from '../../model/Bill';
import { Observable } from 'rxjs';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { BillComponent } from '../bill/bill.component';
import { AsyncPipe } from '@angular/common';
import { SidebarModule } from 'primeng/sidebar';
import { SearchBillsComponent } from '../search-bills/search-bills.component';
import { Button } from 'primeng/button';

@Component({
    selector: 'app-show-bills',
    templateUrl: './show-bills.component.html',
    styleUrls: ['./show-bills.component.css'],
    standalone: true,
  imports: [BillComponent, ProgressSpinnerModule, AsyncPipe, SidebarModule, SearchBillsComponent, Button]
})
export class ShowBillsComponent implements AfterViewChecked {
  @Input() bills$: Observable<Bill[]>;
  @Input() username: string;

  ngAfterViewChecked(): void {
    window.scrollTo(0, document.body.scrollHeight);
  }

}
