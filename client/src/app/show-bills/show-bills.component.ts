import { AfterViewChecked, Component, Input, OnInit } from '@angular/core';
import { Bill } from '../../model/Bill';
import { Observable } from 'rxjs';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { BillComponent } from '../bill/bill.component';
import { AsyncPipe } from '@angular/common';
import { SidebarModule } from 'primeng/sidebar';
import { SearchBillsComponent } from '../search-bills/search-bills.component';
import { Button } from 'primeng/button';
import { PersonService } from '../../services/person/person.service';

@Component({
  selector: 'app-show-bills',
  templateUrl: './show-bills.component.html',
  styleUrls: ['./show-bills.component.css'],
  standalone: true,
  imports: [BillComponent, ProgressSpinnerModule, AsyncPipe, SidebarModule, SearchBillsComponent, Button]
})
export class ShowBillsComponent implements OnInit, AfterViewChecked {
  @Input() bills$: Observable<Bill[]>;
  @Input() username: string;

  constructor(private personService: PersonService) {
  }

  ngOnInit() {
    this.personService.getUsername().subscribe(name => {
      localStorage.setItem('name', name)
    })
  }

  ngAfterViewChecked(): void {
    window.scrollTo(0, document.body.scrollHeight);
  }

}
