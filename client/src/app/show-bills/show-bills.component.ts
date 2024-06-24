import { AfterViewChecked, Component, OnInit } from '@angular/core';
import { Bill } from '../../model/Bill';
import { Observable } from 'rxjs';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { BillComponent } from '../bill/bill.component';
import { AsyncPipe } from '@angular/common';
import { SidebarModule } from 'primeng/sidebar';
import { SearchBillsComponent } from '../search-bills/search-bills.component';
import { Button } from 'primeng/button';
import { BillService } from '../../services/bill/bill.service';
import { PersonService } from '../../services/person/person.service';
import { BillFormComponent } from '../bill-form/bill-form.component';
import { DialogModule } from 'primeng/dialog';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-show-bills',
  templateUrl: './show-bills.component.html',
  styleUrls: ['./show-bills.component.css'],
  standalone: true,
  imports: [BillComponent, ProgressSpinnerModule, AsyncPipe, SidebarModule, SearchBillsComponent, Button, BillFormComponent, DialogModule, TranslateModule]
})
export class ShowBillsComponent implements OnInit, AfterViewChecked {
  protected readonly Math = Math;
  showEditBillDialog = false;
  bills$: Observable<Bill[]>;
  bill: Bill;
  username: string | null;

  constructor(private billService: BillService, private personService: PersonService) {}

  ngOnInit() {
    this.bills$ = this.billService.getBills();
    this.username = localStorage.getItem('name');
    
    if (!this.username) {
      this.personService.getUsername().subscribe(username => {
        this.username = username
        localStorage.setItem('name', username);
      });
    }
  }

  ngAfterViewChecked(): void {
    window.scrollTo(0, document.body.scrollHeight);
  }

  handleEditBillDialog(bill: Bill) {
    this.bill = bill;
    this.showEditBillDialog = true;
  }

  handleEditBill(bill: Bill) {
    bill.id = this.bill.id;
    bill.date = this.bill.date;
    this.billService.editBill(bill).subscribe(() => {
      location.reload()
    });
  }

}
