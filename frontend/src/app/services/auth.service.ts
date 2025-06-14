// src/app/services/auth.service.ts
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly JWT_TOKEN_KEY = 'jwt_token';
  private _isLoggedIn = new BehaviorSubject<boolean>(this.hasToken());
  public isLoggedIn$: Observable<boolean> = this._isLoggedIn.asObservable();

  constructor() { }


  getToken(): string | null {
    return localStorage.getItem(this.JWT_TOKEN_KEY);
  }


  setToken(token: string): void {
    localStorage.setItem(this.JWT_TOKEN_KEY, token);
    this._isLoggedIn.next(true);
  }

  removeToken(): void {
    localStorage.removeItem(this.JWT_TOKEN_KEY);
    this._isLoggedIn.next(false);
  }

  private hasToken(): boolean {
    return !!this.getToken();
  }

  loginSuccess(token: string): void {
    this.setToken(token);
  }

  logout(): void {
    this.removeToken();
  }
}
