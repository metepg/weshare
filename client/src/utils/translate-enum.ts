import { TranslateService } from '@ngx-translate/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

export function getTranslatedEnum(translate: TranslateService, categoryCode: number): Observable<string> {
  return translate.get(`Category.${categoryCode}`).pipe(
    map((val: unknown) => typeof val === 'string' ? val : String(val))
  );
}
