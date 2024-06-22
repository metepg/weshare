import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SidebarService {
  private sidebarVisibilitySubject = new Subject<boolean>();
  sidebarVisibility$ = this.sidebarVisibilitySubject.asObservable();

  toggleSidebar(visible: boolean) {
    this.sidebarVisibilitySubject.next(visible);
  }
}
