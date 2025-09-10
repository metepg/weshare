import { Component, inject } from '@angular/core';
import { BillService } from '../../services/bill/bill.service';
import { Bill } from '../../model/Bill';
import { HttpStatusCode } from '@angular/common/http';
import { CardModule } from 'primeng/card';
import { Router } from '@angular/router';
import { BillFormComponent } from '../bill-form/bill-form.component';
import { MessageService } from 'primeng/api';
import { DebtService } from '../../services/debt/debt.service';
import { LocalStorageService } from '../../services/local-storage/local-storage.service';
import { UserService } from '../../services/user/user.service';
import { TranslatePipe } from '@ngx-translate/core';

@Component({
  selector: 'app-new-bill-form',
  standalone: true,
  templateUrl: './new-bill.component.html',
  styleUrls: ['./new-bill.component.css'],
  imports: [CardModule, BillFormComponent, TranslatePipe]
})
export class NewBillComponent {
  private readonly billService = inject(BillService);
  private readonly router = inject(Router);
  private readonly messageService = inject(MessageService);
  private readonly debtService = inject(DebtService);
  private readonly localStorageService = inject(LocalStorageService);
  private readonly userService = inject(UserService);

  handleSubmit(bill: Bill) {
    this.billService.createBill(bill).subscribe((response) => {
      if (response.status === HttpStatusCode.Created) {
        this.messageService.add({severity: 'success', summary: `Tallennus onnistui.`,});

        const currentUser = this.localStorageService.getUser();
        if (!currentUser) return;

        this.userService.getTotalDebtAmount(currentUser.id).subscribe((amount) => {
          this.debtService.setDebt(amount)
        })
        void this.router.navigate(['bills'])
      }
    });
  }

}
