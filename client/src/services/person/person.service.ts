import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PersonService {

  private apiUrl = environment.apiUrl + '/persons';

  constructor(private http: HttpClient) {
  }

  getUsername(): Observable<string> {
    return this.http.get(`${this.apiUrl}/current`, {responseType: 'text'});
  }
}
