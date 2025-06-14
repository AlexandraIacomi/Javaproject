import {CartItemDto} from './cart-item.model';
import {UserDto} from './user.model';

export interface CartDto {
  id: number;
  user: UserDto;
  items: CartItemDto[];
  totalPrice: number;
}
