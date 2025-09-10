import { Component, inject, input, output } from '@angular/core';
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
  private readonly router = inject(Router);

  protected readonly View = View;
  private readonly routeMap = {
    [View.NEW_BILL]: 'create',
    [View.SHOW_BILLS]: 'bills',
    [View.SHOW_STATISTICS]: 'stats'
  };
  readonly debtAmount = input<number>(0);
  debtEmitter = output<undefined>();

  showTab(view: View) {
    const route = this.routeMap[view] || '';
    void this.router.navigate([route]);
  }

  payDebt() {
    this.debtEmitter.emit(undefined);
  }

}
