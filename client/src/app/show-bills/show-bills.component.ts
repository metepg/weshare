import { AfterViewChecked, Component, computed, inject, signal } from '@angular/core';
import { Bill } from '../../model/Bill';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { BillComponent } from '../bill/bill.component';
import { BillService } from '../../services/bill/bill.service';
import { UserService } from '../../services/user/user.service';
import { BillFormComponent } from '../bill-form/bill-form.component';
import { DialogModule } from 'primeng/dialog';
import { TranslateModule } from '@ngx-translate/core';
import { DebtService } from '../../services/debt/debt.service';
import { EMPTY, switchMap } from 'rxjs';
import { MessageService } from 'primeng/api';
import { CategoryService } from '../../services/category/category.service';
import { toSignal } from '@angular/core/rxjs-interop';
import { User } from '../../model/User';

@Component({
  selector: 'app-show-bills',
  templateUrl: './show-bills.component.html',
  styleUrls: ['./show-bills.component.css'],
  imports: [BillComponent, ProgressSpinnerModule, BillFormComponent, DialogModule, TranslateModule]
})
export class ShowBillsComponent implements AfterViewChecked {
  private readonly billService = inject(BillService);
  private readonly userService = inject(UserService);
  private readonly debtService = inject(DebtService);
  private readonly messageService = inject(MessageService);
  private readonly categoryService = inject(CategoryService);

  protected readonly Math = Math;
  showEditBillDialog = false;
  bills = this.billService.getBills();
  readonly bill = signal<Bill>({} as Bill);
  readonly user = toSignal(this.userService.getCurrentUser(), { initialValue: {} as User });
  readonly userId = computed(() => this.user().id);
  readonly categories = computed(() => {
    return this.categoryService.findCategoriesByGroupId(this.user().groupId);
  });

  ngAfterViewChecked(): void {
    window.scrollTo(0, document.body.scrollHeight);
  }

  handleEditBillDialog(bill: Bill) {
    if (this.userId() !== bill.ownerId) {
      return;
    }
    this.bill.set(bill);
    this.showEditBillDialog = true;
  }

  /**
   * Edits the given bill and updates the local bills and debt amount.
   *
   * @param bill The bill object with updated values.
   */
  handleEditBill(bill: Bill) {
    bill.id = this.bill().id;
    bill.date = this.bill().date;
    this.billService.updateBill(bill).pipe(
      switchMap((updatedBill) => {
        const current = this.bills.value() ?? [];
        this.bills.set(
          current.map((b) => b.id === updatedBill.id ? updatedBill : b)
        );
        this.showEditBillDialog = false;
        const userId = this.userId();

        if (!userId) {
          return EMPTY;
        }

        return this.userService.getTotalDebtAmount(userId);
      })
    ).subscribe((amount) => {
      this.messageService.add({ severity: 'success', summary: 'Muokkaus onnistui.' });
      this.debtService.setDebt(amount);
    });
  }

  handleDeleteBill(id: number) {
    this.billService.deleteBillById(id).pipe(
      switchMap(() => {
        const current = this.bills.value() ?? [];
        this.bills.set(current.filter((b) => b.id !== id));
        this.showEditBillDialog = false;

        const userId = this.userId();

        if (!userId) {
          return EMPTY;
        }

        return this.userService.getTotalDebtAmount(userId);
      })
    ).subscribe((amount) => {
      this.messageService.add({ severity: 'success', summary: 'Laskun poistaminen onnistui.' });
      this.debtService.setDebt(amount);
    });
  }
}
