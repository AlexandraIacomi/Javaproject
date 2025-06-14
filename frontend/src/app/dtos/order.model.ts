import {OrderItem} from './orderitem.model';

export interface OrderDto {
  customerName?: string;
  address?: string;
  phoneNumber?: string;
  orderItems: OrderItem[];
}
