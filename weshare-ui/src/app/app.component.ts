import { Component } from '@angular/core';
import { BillService } from '../services/bill/bill.service';

@Component({
  selector: 'app-root',
  providers: [BillService],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'weshare-ui';
}
