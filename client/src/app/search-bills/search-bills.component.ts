import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { InputIconModule } from 'primeng/inputicon';
import { IconFieldModule } from 'primeng/iconfield';
import { InputTextModule } from 'primeng/inputtext';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MultiSelectModule } from 'primeng/multiselect';
import { BillService } from '../../services/bill/bill.service';
import { MessageService } from 'primeng/api';
import { Bill } from '../../model/Bill';
import { TableModule } from 'primeng/table';
import { DatePipe, DecimalPipe } from '@angular/common';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
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
import { TranslatePipe } from '@ngx-translate/core';
import { toSignal } from '@angular/core/rxjs-interop';

type CategoryOption = { label: string; value: number };
type UserOption = { label: string; value: string };
type SearchForm = {
  description: FormControl<string | null>;
  categories: FormControl<CategoryOption[] | null>;
  range: FormControl<(Date | null)[] | null>;
  users: FormControl<UserOption[] | null>;
}

@Component({
  selector: 'app-search-bills',
  imports: [
    InputIconModule,
    IconFieldModule,
    InputTextModule,
    FormsModule,
    MultiSelectModule,
    ReactiveFormsModule,
    TableModule,
    DecimalPipe,
    DatePipe,
    ProgressSpinnerModule,
    BillFormComponent,
    DialogModule,
    Button,
    Drawer,
    TranslatePipe
  ],
  templateUrl: './search-bills.component.html',
  styleUrl: './search-bills.component.scss'
})
export class SearchBillsComponent implements OnInit {
  private readonly formBuilder = inject(FormBuilder);
  private readonly billService = inject(BillService);
  private readonly primengConfig = inject(PrimeNG);
  private readonly sidebarService = inject(SidebarService);
  private readonly translationService = inject(TranslationService);
  private readonly userService = inject(UserService);
  private readonly messageService = inject(MessageService);
  private readonly localStorageService = inject(LocalStorageService);

  protected readonly Math = Math;
  readonly sidebarVisible = signal(true);
  readonly isLoading = signal(false);
  readonly showEditBillDialog = signal(false);
  readonly selectedBill = signal<Bill | null>(null);
  readonly bills = signal<Bill[]>([]);
  readonly currentUser = signal<User | null>(null);

  searchForm!: FormGroup<SearchForm>;

  private readonly translatedCategories = toSignal(this.translationService.getTranslatedCategories(), {
    initialValue: [] as {
      label: string;
      value: number;
    }[]
  });
  private readonly usersFromApi = toSignal(this.userService.getUsers(), { initialValue: [] });

  readonly categories = computed(() => this.translatedCategories());
  readonly users = computed(() => this.usersFromApi().map((user) => ({ label: user.name, value: user.name })));

  readonly selectedItemsLabel = computed(() => {
    const selectedCategory = this.searchForm.get('categories')?.value;
    if (!Array.isArray(selectedCategory)) {
      return '';
    }
    if (selectedCategory.length === this.categories().length) return 'Kaikki kategoriat';
    return `${selectedCategory.length} valittuna`;
  });

  ngOnInit() {
    this.currentUser.set(this.localStorageService.getUser());
    this.searchForm = this.formBuilder.group<SearchForm>({
      description: this.formBuilder.control<string | null>(null),
      categories: this.formBuilder.control<CategoryOption[]>([]),
      range: this.formBuilder.control<(Date | null)[]>([]),
      users: this.formBuilder.control<UserOption[]>([]),
    });
    if (this.categories().length) {
      this.searchForm.patchValue({ categories: [...this.categories()] }, { emitEvent: false });
    }

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

  onSubmit() {
    const filter: SearchFilter = {
      description: this.searchForm.controls.description.value ?? '',
      categories: this.searchForm.controls.categories.value?.map((category) => category.value) ?? [],
      range: this.searchForm.controls.range.value?.filter((date): date is Date => !!date) ?? [],
      users: this.searchForm.controls.users.value?.map((user) => user.value) ?? [],
    };

    this.sidebarVisible.set(false);
    this.isLoading.set(true);

    this.billService.getBillsByFilter(filter).subscribe({
      next: (response) => {
        this.bills.set(response.body?.filter((bill) => bill.ownAmount !== 0) ?? []);
        this.isLoading.set(false);
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Haku epäonnistui.' });
        this.isLoading.set(false);
      }
    });
  }

  handleSidebarHide() {
    this.sidebarService.toggleSidebar(false);
  }

  showEditView(bill: Bill) {
    this.selectedBill.set(bill);
    this.showEditBillDialog.set(true);
  }

  handleEditBill(selected: Bill): void {
    const id = this.selectedBill()?.id;
    const date = this.selectedBill()?.date;

    if (!id || !date) {
      return;
    }

    selected.setId(id);
    selected.setDate(date);

    this.billService.updateBill(selected).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Kategorian päivitys onnistui' });
        this.bills.update((bills) => bills.map((bill) => bill.id === selected.id ? selected : bill));
        this.showEditBillDialog.set(false);
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Kategorian päivitys epäonnistui.' });
      }
    });
  }
}