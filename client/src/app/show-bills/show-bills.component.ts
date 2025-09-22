import { Component, computed, effect, inject, signal } from '@angular/core';
import { Bill } from '../../model/Bill';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { BillComponent } from '../bill/bill.component';
import { BillService } from '../../services/bill/bill.service';
import { UserService } from '../../services/user/user.service';
import { BillFormComponent } from '../bill-form/bill-form.component';
import { DialogModule } from 'primeng/dialog';
import { DebtService } from '../../services/debt/debt.service';
import { EMPTY, switchMap } from 'rxjs';
import { MessageService } from 'primeng/api';
import { CategoryService } from '../../services/category/category.service';
import { toSignal } from '@angular/core/rxjs-interop';
import { User } from '../../model/User';
import { TranslatePipe } from '@ngx-translate/core';

@Component({
  selector: 'app-show-bills',
  templateUrl: './show-bills.component.html',
  styleUrls: ['./show-bills.component.css'],
  imports: [BillComponent, ProgressSpinnerModule, BillFormComponent, DialogModule, TranslatePipe]
})
export class ShowBillsComponent {
  private readonly billService = inject(BillService);
  private readonly userService = inject(UserService);
  private readonly debtService = inject(DebtService);
  private readonly messageService = inject(MessageService);
  private readonly categoryService = inject(CategoryService);

  showEditBillDialog = false;
  bills = this.billService.bills;
  readonly bill = signal<Bill>({} as Bill);
  readonly user = toSignal(this.userService.getCurrentUser(), { initialValue: {} as User });
  readonly userId = computed(() => this.user().id);
  readonly categories = computed(() => this.categoryService.findCategoriesByGroupId(this.user().groupId));

  constructor() {
    effect(() => {
      this.bills();
      queueMicrotask(() => { window.scrollTo(0, document.body.scrollHeight); });
    });
  }

  get sliderPercent() {
    return Math.round((this.bill().ownAmount / this.bill().amount) * 100);
  }

  handleEditBillDialog(bill: Bill) {
    if (this.userId() !== bill.ownerId) {
      return;
    }
    this.bill.set(bill);
    this.showEditBillDialog = true;
  }

  handleEditBill(bill: Bill) {
    bill.id = this.bill().id;
    bill.date = this.bill().date;
    this.billService.updateBill(bill).subscribe(() => {
      this.showEditBillDialog = false;
      this.messageService.add({ severity: 'success', summary: 'Muokkaus onnistui.' });
    });
  }

  handleDeleteBill(id: number) {
    this.billService.deleteBillById(id).pipe(
      switchMap(() => {
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