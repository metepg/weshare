import { Injectable } from '@angular/core';
import { User } from '../../model/User';
import { Category } from '../../model/Category';

@Injectable({
  providedIn: 'root'
})
export class LocalStorageService {

  setUser(user: User) {
    localStorage.setItem('user', JSON.stringify(user))
  }

  getUser(): User | null {
    const userFromStorage = localStorage.getItem('user');
    return userFromStorage ? (JSON.parse(userFromStorage) as unknown as User) : null;
  }

  setCategories(categories: Category[]) {
    localStorage.setItem('categories', JSON.stringify(categories))
  }

}
