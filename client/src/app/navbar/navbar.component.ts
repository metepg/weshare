import { Component, EventEmitter, Input, Output } from '@angular/core';
import { View } from '../../utils/View';
import { DecimalPipe } from '@angular/common';
import { Button } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
  standalone: true,
  imports: [ToastModule, ToolbarModule, Button, DecimalPipe]
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
    console.log(tab)
    this.tabEmitter.emit(tab)
  }
}
