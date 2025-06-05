import { Component, EventEmitter, Input, Output } from '@angular/core';
import { View } from '../../constants/View';
import { DecimalPipe } from '@angular/common';
import { Button } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { ToastModule } from 'primeng/toast';
import { TranslateModule } from '@ngx-translate/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
  imports: [ToastModule, ToolbarModule, Button, DecimalPipe, TranslateModule]
})
export class NavbarComponent {
  protected readonly View = View;
  private routeMap = {
    [View.NEW_BILL]: 'create',
    [View.SHOW_BILLS]: 'bills',
    [View.SHOW_STATISTICS]: 'stats'
  };
  @Input() debtAmount: number;
  @Output() debtEmitter = new EventEmitter<void>();

  constructor(private router: Router) {}

  showTab(view: View) {
    const route = this.routeMap[view] || '';
    this.router.navigate([route]);
  }

  payDebt() {
    this.debtEmitter.emit();
  }

}
