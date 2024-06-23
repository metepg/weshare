import { Component } from '@angular/core';
import { BillService } from '../../services/bill/bill.service';
import { Bill } from '../../model/Bill';
import { HttpStatusCode } from '@angular/common/http';
import { CardModule } from 'primeng/card';
import { Router } from '@angular/router';
import { BillFormComponent } from '../bill-form/bill-form.component';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-new-bill-form',
  templateUrl: './new-bill-form.component.html',
  styleUrls: ['./new-bill-form.component.css'],
  standalone: true, 
  imports: [CardModule, BillFormComponent, TranslateModule]
})
export class NewBillFormComponent {

  constructor(private billService: BillService, private router: Router) {}
  
  handleSubmit(bill: Bill) {
    this.billService.createBill(bill).subscribe((response) => {
      if (response.status === HttpStatusCode.Created) {
        this.billService.notifyBillCreated(bill.amount);
        this.router.navigate(['bills'])
      }
    });
  }
  
}
