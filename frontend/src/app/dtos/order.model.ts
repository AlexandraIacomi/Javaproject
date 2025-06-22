import {OrderItem} from './orderitem.model';

export interface OrderDto {
  id?: number;
  paypalOrderId?: string;
  customerName?: string;
  address?: string;
  phoneNumber?: string;
  orderItems: OrderItem[];
}
