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
    if (!userFromStorage) {
      return null;
    } else {
      return JSON.parse(userFromStorage);
    }
  }
  
  setCategories(categories: Category[]) {
    localStorage.setItem('categories', JSON.stringify(categories))
  }

  getCategories(): Category[] | null {
    const categoriesFromStorage = localStorage.getItem('categories');
    if (!categoriesFromStorage) {
      return null;
    } else {
      return JSON.parse(categoriesFromStorage);
    }
  }
  
}
