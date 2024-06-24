import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Bill } from '../../model/Bill';
import { environment } from '../../environments/environment';
import { SearchFilter } from '../../model/SearchFilter';

@Injectable({providedIn: 'root'})
export class BillService {
  private apiUrl = environment.apiUrl + '/bills';

  constructor(private http: HttpClient) {
  }

  createBill(bill: Bill): Observable<HttpResponse<Bill>> {
    return this.http.post<Bill>(`${this.apiUrl}/create`, bill, {
      observe: 'response',
    });
  }

  getBillCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }

  getBills(): Observable<Bill[]> {
    return this.http.get<Bill[]>(`${this.apiUrl}`);
  }

  getBillsByYear(year: number): Observable<Bill[]> {
    return this.http.get<Bill[]>(`${this.apiUrl}/statistics/` + year);
  }

  payDebt(bill: Bill): Observable<HttpResponse<Bill[]>> {
    return this.http.post<Bill[]>(
      `${this.apiUrl}/pay`, bill,
      {observe: 'response'}
    );
  }

  getTotalDebtAmount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/debt`);
  }

  getTotalAmountByUserName(name: string): Observable<Bill[]> {
    return this.http.get<Bill[]>(`${this.apiUrl}/total/${name}`);
  }
  
  getBillsByFilter(searchFilter: SearchFilter): Observable<HttpResponse<Bill[]>> {
    return this.http.post<Bill[]>(`${this.apiUrl}`, searchFilter, {
      observe: 'response',
    });
  }

  editBill(bill: Bill): Observable<Bill> {
    const url = `${this.apiUrl}`;
    return this.http.put<Bill>(url, bill);
  }

  deleteBillById(id: number): Observable<boolean> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.delete<boolean>(url);
  }
  
}
