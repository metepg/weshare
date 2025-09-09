import { inject, Injectable, Signal } from '@angular/core';
import { HttpClient, httpResource, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Bill } from '../../model/Bill';
import { environment } from '../../environments/environment';
import { SearchFilter } from '../../model/SearchFilter';

@Injectable({providedIn: 'root'})
export class BillService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = environment.apiUrl + '/bills';

  readonly getBills = () =>
    httpResource<Bill[]>(() => `${this.apiUrl}`);

  readonly getBillsByYear = (year: Signal<number>) =>
    httpResource<Bill[]>(() => `${this.apiUrl}/statistics/${year()}`);

  readonly getBillsByUserId = (userId: Signal<number>) =>
    httpResource<Bill[]>(() => `${this.apiUrl}/user/${userId()}`);

  createBill(bill: Bill): Observable<HttpResponse<Bill>> {
    return this.http.post<Bill>(`${this.apiUrl}`, bill, {
      observe: 'response',
    });
  }

  payDebt(bill: Bill): Observable<HttpResponse<Bill[]>> {
    return this.http.post<Bill[]>(
      `${this.apiUrl}/pay`, bill, {observe: 'response'}
    );
  }

  getBillsByFilter(searchFilter: SearchFilter): Observable<HttpResponse<Bill[]>> {
    return this.http.post<Bill[]>(`${this.apiUrl}/search`, searchFilter, {
      observe: 'response',
    });
  }

  updateBill(bill: Bill): Observable<Bill> {
    return this.http.put<Bill>(`${this.apiUrl}`, bill);
  }

  deleteBillById(id: number): Observable<boolean> {
    return this.http.delete<boolean>(`${this.apiUrl}/${id}`);
  }
}
