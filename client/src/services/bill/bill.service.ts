import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { Bill } from '../../model/Bill';
import { environment } from '../../environments/environment';
import { SearchFilter } from '../../model/SearchFilter';

@Injectable({providedIn: 'root'})
export class BillService {
  private apiUrl = environment.apiUrl + '/bills';
  private billCreatedSubject = new Subject<number>();
  billCreated$ = this.billCreatedSubject.asObservable();

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

  getTotalAmount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/total`);
  }

  getBillsByFilter(searchFilter: SearchFilter): Observable<HttpResponse<Bill[]>> {
    return this.http.post<Bill[]>(`${this.apiUrl}`, searchFilter, {
      observe: 'response',
    });
  }

  notifyBillCreated(amount: number) {
    this.billCreatedSubject.next(amount);
  }
}
