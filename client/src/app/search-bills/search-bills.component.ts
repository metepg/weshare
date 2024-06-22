import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { InputIconModule } from 'primeng/inputicon';
import { IconFieldModule } from 'primeng/iconfield';
import { InputTextModule } from 'primeng/inputtext';
import { DropdownModule } from 'primeng/dropdown';
import { CalendarModule } from 'primeng/calendar';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule
} from '@angular/forms';
import { MultiSelectModule } from 'primeng/multiselect';
import { BillCategoryCode } from '../../constants/Categories';
import { BillService } from '../../services/bill/bill.service';
import { PrimeNGConfig } from 'primeng/api';
import { USERS } from '../../constants/constants';
import { Bill } from '../../model/Bill';
import { TableModule } from 'primeng/table';
import { DatePipe, DecimalPipe } from '@angular/common';
import { SidebarModule } from 'primeng/sidebar';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { getTranslatedEnum } from '../../utils/translate-enum';
import { forkJoin, map } from 'rxjs';
import { SidebarService } from '../../services/sidebar/sidebar.service';

@Component({
  selector: 'app-search-bills',
  standalone: true,
  imports: [
    InputIconModule,
    IconFieldModule,
    InputTextModule,
    DropdownModule,
    CalendarModule,
    FormsModule,
    MultiSelectModule,
    ReactiveFormsModule,
    TableModule,
    DecimalPipe,
    DatePipe,
    SidebarModule,
    ProgressSpinnerModule,
    TranslateModule
  ],
  templateUrl: './search-bills.component.html',
  styleUrl: './search-bills.component.scss'
})
export class SearchBillsComponent implements OnInit {
  bills: Bill[];
  categories: { label: string, value: number }[];
  users: { label: string, value: string }[];
  searchForm: FormGroup;
  isLoading = false;
  @Input() sidebarVisible = true;
  @Output() sidebarVisibleChange = new EventEmitter<boolean>();

  constructor(private formBuilder: FormBuilder,
              private billService: BillService,
              private primengConfig: PrimeNGConfig,
              private translate: TranslateService,
              private sidebarService: SidebarService
              ) {
    const translations$ = Object.values(BillCategoryCode)
    .filter(value => typeof value === 'number')
    .map(value =>
      getTranslatedEnum(this.translate, value as BillCategoryCode).pipe(
        map(label => ({label, value: value as BillCategoryCode}))
      )
    );

    forkJoin(translations$).subscribe(translatedCategories => {
      this.categories = translatedCategories;
    });

    this.users = USERS.map(user => {
      return {
        label: user,
        value: user,
      }
    })
  }

  ngOnInit() {
    this.categories = this.categories || [];
    this.sidebarService.sidebarVisibility$.subscribe(visible => {
      this.sidebarVisible = visible;
    });
    
    this.searchForm = this.formBuilder.group({
      description: [null],
      categories: [[...this.categories]],
      range: [null],
      users: [[...this.users]],
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
    const searchFilter = {
      description,
      categories,
      range,
      users
    };

    this.sidebarVisible = false;
    this.isLoading = true;
    this.billService.getBillsByFilter(searchFilter).subscribe(response => {
      if (response.ok && response.body) {
        this.bills = response.body;
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
}
