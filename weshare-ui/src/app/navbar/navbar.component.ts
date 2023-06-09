import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Constants } from '../../utils/Constants';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  NEW_BILL = Constants.NEW_BILL;
  SHOW_BILLS = Constants.SHOW_BILLS;
  SHOW_STATISTICS = Constants.SHOW_STATISTICS;

  @Input() debtAmount: number;
  @Output() tabEmitter = new EventEmitter<number>();
  @Output() debtEmitter = new EventEmitter<boolean>();

  constructor() {
  }

  ngOnInit(): void {
  }

  payDebt(): void {
    this.debtEmitter.emit();
  }

  showTab(tab: number): void {
    this.tabEmitter.emit(tab)
  }

}
