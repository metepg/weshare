import { AfterViewChecked, Component, OnInit } from '@angular/core';
import { Bill } from '../../model/Bill';
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
import { DebtService } from '../../services/debt/debt.service';
import { switchMap } from 'rxjs';
import { MessageService } from 'primeng/api';

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
  bills: Bill[] = [];
  bill: Bill;
  username: string | null;

  constructor(
    private billService: BillService,
    private personService: PersonService,
    private debtService: DebtService,
    private messageService: MessageService) {}

  ngOnInit() {
    this.billService.getBills().subscribe(bills => {
      this.bills = bills;
    });
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

  /**
   * Edits the given bill and updates the local bills and debt amount.
   *
   * @param bill The bill object with updated values.
   */
  handleEditBill(bill: Bill) {
    bill.id = this.bill.id;
    bill.date = this.bill.date;
    this.billService.editBill(bill).pipe(
      switchMap(updatedBill => {
        this.bills = this.bills.map(bill => bill.id === updatedBill.id ? updatedBill : bill);
        this.showEditBillDialog = false;
        return this.billService.getTotalAmount();
      })
    ).subscribe(amount => {
      if (amount) {
        this.messageService.add({severity: 'success', summary: `Muokkaus onnistui.`,});
        this.debtService.setDebt(amount)
      } else {
        this.messageService.add({severity: 'danger', summary: `Muokkaus epäonnistui.`});
      }
    });
  }

}
