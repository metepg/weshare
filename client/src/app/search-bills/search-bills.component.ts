import { Component, OnInit } from '@angular/core';
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
import { BillCategoryCode } from '../../utils/Categories';
import { BillService } from '../../services/bill/bill.service';
import { PrimeNGConfig } from 'primeng/api';

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
    ReactiveFormsModule
  ],
  templateUrl: './search-bills.component.html',
  styleUrl: './search-bills.component.scss'
})
export class SearchBillsComponent implements OnInit {

  categories: { label: string, value: number }[];
  searchForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private billService: BillService, private primengConfig: PrimeNGConfig) {
    this.categories = Object.keys(BillCategoryCode)
    .filter((key) => isNaN(Number(key)))
    .map((key, index) => ({
      label: key,
      value: index
    }));
  }
  

  ngOnInit() {
    this.searchForm = this.formBuilder.group({
      description: [null],
      categories: [[...this.categories]],
      range: [null],
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
    
    if (selectedCategories.length === this.categories.length) {
      return 'Kaikki';
    }
    return `${selectedCategories.length} valittuna`;
  }

  onSubmit() {
    const description = this.searchForm.get('description')?.value ?? '';
    const categories = this.searchForm?.get('categories')?.value?.map((category: { label: string, value: number }) => category.value) || [];
    const range = (this.searchForm?.get('range')?.value || []).filter((date: Date | null) => date !== null);
    const searchFilter = {
      description,
      categories,
      range
    };
    console.log(searchFilter);
    this.billService.getBillsByFilter(searchFilter).subscribe(response => {
      if (response.ok) {
        console.log(response.body)
      }
    })
  }

}
