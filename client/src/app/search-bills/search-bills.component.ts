import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { InputIconModule } from 'primeng/inputicon';
import { IconFieldModule } from 'primeng/iconfield';
import { InputTextModule } from 'primeng/inputtext';
import { CalendarModule } from 'primeng/calendar';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule
} from '@angular/forms';
import { MultiSelectModule } from 'primeng/multiselect';
import { BillService } from '../../services/bill/bill.service';
import { MessageService} from 'primeng/api';
import { Bill } from '../../model/Bill';
import { TableModule } from 'primeng/table';
import { DatePipe, DecimalPipe } from '@angular/common';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { TranslateModule } from '@ngx-translate/core';
import { SidebarService } from '../../services/sidebar/sidebar.service';
import { TranslationService } from '../../services/translation/translation.service';
import { UserService } from '../../services/user/user.service';
import { BillFormComponent } from '../bill-form/bill-form.component';
import { DialogModule } from 'primeng/dialog';
import { SearchFilter } from '../../model/SearchFilter';
import { LocalStorageService } from '../../services/local-storage/local-storage.service';
import { User } from '../../model/User';
import { PrimeNG } from 'primeng/config';
import { Button } from 'primeng/button';
import { Drawer } from 'primeng/drawer';

@Component({
  selector: 'app-search-bills',
  imports: [
    InputIconModule,
    IconFieldModule,
    InputTextModule,
    CalendarModule,
    FormsModule,
    MultiSelectModule,
    ReactiveFormsModule,
    TableModule,
    DecimalPipe,
    DatePipe,
    ProgressSpinnerModule,
    TranslateModule,
    BillFormComponent,
    DialogModule,
    Button,
    Drawer
  ],
  templateUrl: './search-bills.component.html',
  styleUrl: './search-bills.component.scss'
})
export class SearchBillsComponent implements OnInit {
  protected readonly Math = Math;
  selectedBill: Bill;
  bills: Bill[];
  currentUser: User | null;
  categories: { label: string, value: number }[];
  users: { label: string, value: string }[];
  searchForm: FormGroup;
  isLoading = false;
  showEditBillDialog = false;
  searchFilter: SearchFilter;
  @Input() sidebarVisible = true;
  @Output() sidebarVisibleChange = new EventEmitter<boolean>();

  constructor(private formBuilder: FormBuilder,
              private billService: BillService,
              private primengConfig: PrimeNG,
              private sidebarService: SidebarService,
              private translationService: TranslationService,
              private userService: UserService,
              private messageService: MessageService,
              private localStorageService: LocalStorageService,
              ) {}

  ngOnInit() {
    this.currentUser = this.localStorageService.getUser();
    this.searchForm = this.formBuilder.group({
      description: [null],
      categories: [null],
      range: [null],
      users: [null],
    });
    
    this.translationService.getTranslatedCategories().subscribe(translatedCategories => {
      this.categories = translatedCategories;
      this.searchForm.patchValue({
        categories: [...this.categories]
      });
      this.userService.getUsers().subscribe(users => {
        this.users = users.map(user => {
          return {
            label: user.name,
            value: user.name,
          }
        })
      })
    });

    this.sidebarService.sidebarVisibility$.subscribe(visible => {
      this.sidebarVisible = visible;
    });

    // TODO: Put these to translation file
    this.primengConfig.setTranslation({
      firstDayOfWeek: 1,
      dayNames: ['Sunnuntai', 'Maanantai', 'Tiistai', 'Keskiviikko', 'Torstai', 'Perjantai', 'Lauantai'],
      dayNamesShort: ['su', 'ma', 'ti', 'ke', 'to', 'pe', 'la'],
      dayNamesMin: ['Su', 'Ma', 'Ti', 'Ke', 'To', 'Pe', 'La'],
      monthNames: ['Tammikuu', 'Helmikuu', 'Maaliskuu', 'Huhtikuu', 'Toukokuu', 'Kesäkuu', 'Heinäkuu', 'Elokuu', 'Syyskuu', 'Lokakuu', 'Marraskuu', 'Joulukuu'],
      monthNamesShort: ['tam', 'hel', 'maa', 'huh', 'tou', 'kes', 'hei', 'elo', 'syy', 'lok', 'mar', 'jou'],
      today: 'Tänään',
      clear: 'Tyhjennä'
    });
  }
  
  getSelectedItemsLabel() {
    const selectedCategories = this.searchForm.get('categories')?.value;
    if (!selectedCategories) return;
    
    if (selectedCategories.length === this.categories?.length) {
      return 'Kaikki kategoriat';
    }
    return `${selectedCategories.length} valittuna`;
  }

  onSubmit() {
    const description = this.searchForm.get('description')?.value ?? '';
    const categories = this.searchForm?.get('categories')?.value?.map((category: { label: string, value: number }) => category.value) || [];
    const range = (this.searchForm?.get('range')?.value || []).filter((date: Date | null) => date !== null);
    const users = this.searchForm?.get('users')?.value?.map((user: { label: string, value: string }) => user.value) || [];
    this.searchFilter = {
      description,
      categories,
      range,
      users
    };

    this.sidebarVisible = false;
    this.isLoading = true;
    this.billService.getBillsByFilter(this.searchFilter).subscribe(response => {
      if (response.ok && response.body) {
        const bills = response.body;
        this.bills = bills.filter(bill => bill.ownAmount !== 0);
        this.isLoading = false;
        this.sidebarVisibleChange.emit(this.sidebarVisible);
      } else {
        // TODO: Better error handling
        alert("Jotain meni pieleen.");
      }
    })
  }

  handleSidebarHide() {
    this.sidebarService.toggleSidebar(false);
  }
  
  showEditView(bill: Bill) {
    this.selectedBill = bill;
    this.showEditBillDialog = true;
  }
  
  handleEditBill(bill: Bill): void {
    if (!bill) return;
    
    const {amount, description, id, date, ownAmount, ownerId, ownerName, paid} = this.selectedBill;
    const editedBill = new Bill(amount!, bill.categoryId!, description!, ownAmount!, ownerId, ownerName, paid);
    editedBill.setId(id);
    editedBill.setDate(date);
    this.billService.updateBill(editedBill).subscribe(editedBill => {
      if (!editedBill) {
        this.messageService.add({severity: 'error', summary: `Kategorian päivitys epäonnistui.`,});
      } else {
        this.messageService.add({severity: 'success', summary: `Kategorian päivitys onnistui`,});
        const categoryIds = new Set(this.searchFilter.categories);
        this.bills = this.bills
          .map(b => b.id === editedBill.id ? editedBill : b)
          .filter(b => categoryIds.has(b.categoryId));
        this.showEditBillDialog = false;
      }
    })
  }

}
