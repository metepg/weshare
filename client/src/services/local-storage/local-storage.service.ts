import { Injectable } from '@angular/core';
import { User } from '../../model/User';

@Injectable({
  providedIn: 'root'
})
export class LocalStorageService {

  setUser(user: User) {
    localStorage.setItem('user', JSON.stringify(user))
  }
  
  getUser(): User | null {
    const userFromStorage = localStorage.getItem('user');
    if (!userFromStorage) {
      return null; 
    } else {
      return JSON.parse(userFromStorage);
    }
  }
}
