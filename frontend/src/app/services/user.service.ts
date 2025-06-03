import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

export interface UserDetails {
  fullName: string;
  expirationTime: number;
  roles: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private userDetailsSubject = new BehaviorSubject<UserDetails | null>(this.getUserFromStorage());
  public userDetails$: Observable<UserDetails | null> = this.userDetailsSubject.asObservable();

  constructor() {}

  public getUserFromStorage(): UserDetails | null {
    const userData = sessionStorage.getItem('userDetails');
    return userData ? JSON.parse(userData) : null;
  }

  public setUserDetails(details: UserDetails): void {
    sessionStorage.setItem('userDetails', JSON.stringify(details));

    this.userDetailsSubject.next(details);
  }

  public clearUserDetails(): void {
    sessionStorage.removeItem('userDetails');
    this.userDetailsSubject.next(null);
  }
}
