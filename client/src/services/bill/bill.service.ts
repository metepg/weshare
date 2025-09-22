import { inject, Injectable, signal, Signal } from '@angular/core';
import { HttpClient, httpResource, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Bill } from '../../model/Bill';
import { environment } from '../../environments/environment';
import { SearchFilter } from '../../model/SearchFilter';
import { LocalStorageService } from '../local-storage/local-storage.service';
import { UserService } from '../user/user.service';
import { DebtService } from '../debt/debt.service';

enum BillEventType {
  ADDED = 'ADDED',
  DEBT_PAID = 'DEBT_PAID',
  UPDATED = 'UPDATED',
  DELETED = 'DELETED'
}
type BillEvent = {
  type: BillEventType;
  bill: Bill;
}

@Injectable({ providedIn: 'root' })
export class BillService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = environment.apiUrl + '/bills';

  private readonly localStorageService = inject(LocalStorageService);
  private readonly userService = inject(UserService);
  private readonly debtService = inject(DebtService);

  readonly bills = signal<Bill[]>([]);

  constructor() {
    this.loadBills();
    this.attachToServerSentEvents();
  }

  private loadBills() {
    this.http.get<Bill[]>(this.apiUrl).subscribe((data) => {
      this.bills.set(data)
    });
  }

  readonly getBillsByYear = (year: Signal<number>) =>
    httpResource<Bill[]>(() => `${this.apiUrl}/statistics/${year()}`);

  readonly getBillsByUserId = (userId: Signal<number>) =>
    httpResource<Bill[]>(() => `${this.apiUrl}/user/${userId()}`);

  createBill(bill: Bill): Observable<HttpResponse<Bill>> {
    return this.http.post<Bill>(`${this.apiUrl}`, bill, { observe: 'response' });
  }

  payDebt(bill: Bill): Observable<HttpResponse<Bill[]>> {
    return this.http.post<Bill[]>(`${this.apiUrl}/pay`, bill, { observe: 'response' });
  }

  getBillsByFilter(searchFilter: SearchFilter): Observable<HttpResponse<Bill[]>> {
    return this.http.post<Bill[]>(`${this.apiUrl}/search`, searchFilter, { observe: 'response' });
  }

  updateBill(bill: Bill): Observable<Bill> {
    return this.http.put<Bill>(`${this.apiUrl}`, bill);
  }

  deleteBillById(id: number): Observable<boolean> {
    return this.http.delete<boolean>(`${this.apiUrl}/${id}`);
  }

  private attachToServerSentEvents() {
    const eventSource = new EventSource(`${this.apiUrl}/stream`);

    eventSource.addEventListener("bill-event", (event: MessageEvent) => {
      const billEvent = JSON.parse(event.data as string) as BillEvent;

      switch (billEvent.type) {
        case BillEventType.ADDED:
          this.bills.update((bills) => [...bills, billEvent.bill]);
          this.refreshDebt();
          break;

        case BillEventType.UPDATED:
          this.bills.update((bills) => bills.map((bill) => bill.id === billEvent.bill.id ? billEvent.bill : bill));
          this.refreshDebt();
          break;

        case BillEventType.DELETED:
          this.bills.update((bills) => bills.filter((bill) => bill.id !== billEvent.bill.id));
          this.refreshDebt();
          break;

        case BillEventType.DEBT_PAID:
          this.refreshDebt();
          this.http.get<Bill[]>(this.apiUrl).subscribe((bills) => {
            this.bills.set(bills)
          });
          break;
      }
    });

    eventSource.addEventListener("heartbeat", () => {});

    eventSource.onerror = () => {};
  }

  private refreshDebt() {
    const user = this.localStorageService.getUser();
    if (user) {
      this.userService.getTotalDebtAmount(user.id).subscribe((amount) => {
        this.debtService.setDebt(amount);
      });
    }
  }

}
