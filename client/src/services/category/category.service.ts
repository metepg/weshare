import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Category } from '../../model/Category';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private readonly http = inject(HttpClient);

  private readonly apiUrl = environment.apiUrl + '/categories';

  findCategoriesByGroupId(id: string): Observable<Category[]> {
    return this.http.get<Category[]>(`${this.apiUrl}/${id}`);
  }

}
