import { inject, Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Observable, forkJoin } from 'rxjs';
import { map } from 'rxjs/operators';
import { BillCategoryCode } from '../../constants/Categories';
import { getTranslatedEnum } from '../../utils/translate-enum';

@Injectable({
  providedIn: 'root'
})
export class TranslationService {
  private readonly translate = inject(TranslateService);

  getTranslatedCategories(): Observable<{ label: string; value: BillCategoryCode }[]> {
    const translations$ = Object.values(BillCategoryCode)
      .filter((value) => typeof value === 'number')
      .map((value) =>
        getTranslatedEnum(this.translate, value as BillCategoryCode).pipe(
          map((label) => ({ label, value: value as BillCategoryCode }))
        ));

    return forkJoin(translations$);
  }
}
