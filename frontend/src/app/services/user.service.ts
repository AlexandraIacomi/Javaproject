import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

export interface UserDetails {
  fullName: string;
  expirationTime: number;
  roles: string[];
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

  public setUserDetails(details: UserDetails, token: string): void {
    sessionStorage.setItem('userDetails', JSON.stringify(details));
    sessionStorage.setItem('token', token);

    this.userDetailsSubject.next(details);
  }

  public getToken(): string | null {
    return sessionStorage.getItem('token');
  }

  public clearUserDetails(): void {
    sessionStorage.removeItem('userDetails');
    sessionStorage.removeItem('token');
    this.userDetailsSubject.next(null);
  }
  public isLoggedIn(): boolean {
    return !!this.getToken();
  }
}
