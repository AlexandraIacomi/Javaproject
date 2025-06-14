import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from '../../environments/environment';
import {OrderDto} from '../dtos/order.model';
import {UserService} from './user.service';


@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private apiUrl = `${environment.backendUrl}/order`;

  constructor(private http: HttpClient, private userService: UserService) {}

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

  createOrder(order: OrderDto): Observable<OrderDto> {
    const headers = this.getAuthHeaders();
    return this.http.post<OrderDto>(`${this.apiUrl}/create`, order, { headers });
  }

}
