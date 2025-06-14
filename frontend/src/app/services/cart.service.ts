// src/app/services/cart.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, catchError, tap, throwError } from 'rxjs';

import { UserService } from './user.service';
import { environment } from '../../environments/environment';
import {CartDto} from '../dtos/cart.model';
import {CartItemRequest} from '../dtos/cart-item-request.model';



@Injectable({
  providedIn: 'root'
})
export class CartService {

  private apiUrl = `${environment.backendUrl}/cart`;


  private _cart = new BehaviorSubject<CartDto | null>(null);
  private _cartItemCount = new BehaviorSubject<number>(0);
  private _cartTotalPrice = new BehaviorSubject<number>(0);


  cart$: Observable<CartDto | null> = this._cart.asObservable();
  cartItemCount$: Observable<number> = this._cartItemCount.asObservable();
  cartTotalPrice$: Observable<number> = this._cartTotalPrice.asObservable();

  constructor(
    private http: HttpClient,
    private userService: UserService
  ) {

    this.userService.userDetails$.subscribe(userDetails => {
      if (userDetails) {
        this.loadCart().subscribe({
          error: (err) => console.error('Failed to auto-load cart on startup:', err)
        });
      } else {
        this.clearCartState();
      }
    });
  }


  private getAuthHeaders(): HttpHeaders {
    const token = this.userService.getToken();
    if (token) {
      return new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      });
    }

    return new HttpHeaders({ 'Content-Type': 'application/json' });
  }

  private updateCartState(cart: CartDto | null): void {
    this._cart.next(cart);
    if (cart) {
      const totalCount = cart.items.reduce((sum, item) => sum + item.quantity, 0);
      this._cartItemCount.next(totalCount);
      this._cartTotalPrice.next(cart.totalPrice);
    } else {
      this._cartItemCount.next(0);
      this._cartTotalPrice.next(0);
    }
  }

  private clearCartState(): void {
    this.updateCartState(null);
  }


  loadCart(): Observable<CartDto> {
    return this.http.get<CartDto>(this.apiUrl, { headers: this.getAuthHeaders() }).pipe(
      tap(cart => this.updateCartState(cart)),
      catchError(error => {
        this.updateCartState(null);
        return throwError(() => new Error('Failed to load cart. Please try again.'));
      })
    );
  }


  addProduct(productId: number, quantity: number): Observable<CartDto> {
    const request: CartItemRequest = { productId, quantity };
    return this.http.post<CartDto>(`${this.apiUrl}/add`, request, { headers: this.getAuthHeaders() }).pipe(
      tap(cart => this.updateCartState(cart)),
      catchError(error => {
        return throwError(() => new Error('Failed to add product to cart. Please check inputs and try again.'));
      })
    );
  }


  updateProductQuantity(productId: number, quantity: number): Observable<CartDto> {
    const request: CartItemRequest = { productId, quantity };
    return this.http.put<CartDto>(`${this.apiUrl}/update`, request, { headers: this.getAuthHeaders() }).pipe(
      tap(cart => this.updateCartState(cart)),
      catchError(error => {

        return throwError(() => new Error('Failed to update product quantity.'));
      })
    );
  }


  removeProduct(productId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/remove/${productId}`, { headers: this.getAuthHeaders() }).pipe(
      tap(() => this.loadCart().subscribe()),
      catchError(error => {
        return throwError(() => new Error('Failed to remove product from cart.'));
      })
    );
  }


  clearCart(): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/clear`, { headers: this.getAuthHeaders() }).pipe(
      tap(() => this.updateCartState(null)),
      catchError(error => {
        return throwError(() => new Error('Failed to clear cart.'));
      })
    );
  }

}
