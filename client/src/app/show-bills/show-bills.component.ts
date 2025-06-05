import { AfterViewChecked, Component, OnInit } from '@angular/core';
import { Bill } from '../../model/Bill';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { BillComponent } from '../bill/bill.component';
import { SidebarModule } from 'primeng/sidebar';
import { BillService } from '../../services/bill/bill.service';
import { UserService } from '../../services/user/user.service';
import { BillFormComponent } from '../bill-form/bill-form.component';
import { DialogModule } from 'primeng/dialog';
import { TranslateModule } from '@ngx-translate/core';
import { DebtService } from '../../services/debt/debt.service';
import { switchMap } from 'rxjs';
import { MessageService } from 'primeng/api';
import { User } from '../../model/User';
import { LocalStorageService } from '../../services/local-storage/local-storage.service';
import { CategoryService } from '../../services/category/category.service';

@Component({
  selector: 'app-show-bills',
  templateUrl: './show-bills.component.html',
  styleUrls: ['./show-bills.component.css'],
  imports: [BillComponent, ProgressSpinnerModule, SidebarModule, BillFormComponent, DialogModule, TranslateModule]
})
export class ShowBillsComponent implements OnInit, AfterViewChecked {
  protected readonly Math = Math;
  showEditBillDialog = false;
  bills: Bill[] = [];
  bill: Bill;
  user: User;

  constructor(
    private billService: BillService,
    private userService: UserService,
    private debtService: DebtService,
    private messageService: MessageService,
    private localStorageService: LocalStorageService,
    private categoryService: CategoryService,
  ) {}

  ngOnInit() {
    this.billService.getBills().subscribe((bills) => {
      this.bills = bills;
      this.setUserAndCategories();
    });
  }

  setUserAndCategories() {
    const currentUser = this.localStorageService.getUser();
    if (!currentUser) return;

    this.user = currentUser;

    this.categoryService.findCategoriesByGroupId(this.user.groupId).subscribe((categories) => {
      this.localStorageService.setCategories(categories);
    })
  }

  ngAfterViewChecked(): void {
    window.scrollTo(0, document.body.scrollHeight);
  }

  handleEditBillDialog(bill: Bill) {
    if (this.user.id !== bill.ownerId) return;
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
    this.billService.updateBill(bill).pipe(
      switchMap((updatedBill) => {
        this.bills = this.bills.map((bill) => bill.id === updatedBill.id ? updatedBill : bill);
        this.showEditBillDialog = false;
        return this.userService.getTotalDebtAmount(this.user.id);
      })
    ).subscribe((amount) => {
      this.messageService.add({severity: 'success', summary: `Muokkaus onnistui.`,});
      this.debtService.setDebt(amount)
    });
  }

  handleDeleteBill(id: number) {
    this.billService.deleteBillById(id).pipe(
      switchMap(() => {
        this.bills = this.bills.filter((bill) => bill.id !== id);
        this.showEditBillDialog = false;
        return this.userService.getTotalDebtAmount(this.user.id);
      })
    ).subscribe((amount) => {
      this.messageService.add({severity: 'success', summary: `Laskun poistaminen onnistui.`,});
      this.debtService.setDebt(amount)
    });
  }
}
