import { Component } from '@angular/core';
import { BillService } from '../services/bill/bill.service';
import { RouterOutlet } from '@angular/router';
import { MainComponent } from './main/main.component';

@Component({
    selector: 'app-root',
    providers: [BillService],
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css'],
    standalone: true,
    imports: [MainComponent, RouterOutlet]
})
export class AppComponent {
  title = 'weshare-ui';
}
