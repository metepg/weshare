import { AfterViewChecked, Component, Input, OnInit } from '@angular/core';
import { Bill } from '../../model/Bill';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-show-bills',
  templateUrl: './show-bills.component.html',
  styleUrls: ['./show-bills.component.css']
})
export class ShowBillsComponent implements OnInit, AfterViewChecked {
  @Input() bills$: Observable<Bill[]>;
  @Input() username: string;

  constructor() {
  }

  ngOnInit(): void {
  }

  ngAfterViewChecked(): void {
    window.scrollTo(0, document.body.scrollHeight);
  }


}
