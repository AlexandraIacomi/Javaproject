import {Product} from '../admin/product/product.model';


export interface CartItemDto {
  id: number;
  product: Product;
  quantity: number;
  subtotal: number;
}
