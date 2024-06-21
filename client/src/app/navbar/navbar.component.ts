import { Component, EventEmitter, Input, Output } from '@angular/core';
import { View } from '../../constants/View';
import { DecimalPipe } from '@angular/common';
import { Button } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { ToastModule } from 'primeng/toast';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
  standalone: true,
  imports: [ToastModule, ToolbarModule, Button, DecimalPipe, TranslateModule]
})
export class NavbarComponent {
  protected readonly View = View;
  @Input() debtAmount: number;
  @Output() tabEmitter = new EventEmitter<number>();
  @Output() debtEmitter = new EventEmitter<boolean>();

  payDebt(): void {
    this.debtEmitter.emit();
  }

  showTab(tab: number): void {
    this.tabEmitter.emit(tab)
  }
}
