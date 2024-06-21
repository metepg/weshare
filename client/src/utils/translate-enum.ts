import { TranslateService } from '@ngx-translate/core';
import { Observable } from 'rxjs';

export function getTranslatedEnum(translate: TranslateService, categoryCode: number): Observable<string>{
  return translate.get(`Category.${categoryCode}`);
}
