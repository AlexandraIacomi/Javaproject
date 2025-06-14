package ro.foame.employee_backend.services;


import org.springframework.stereotype.Service;
import ro.foame.employee_backend.dtos.CartDto;
import ro.foame.employee_backend.entities.Cart;
import ro.foame.employee_backend.entities.CartItem;
import ro.foame.employee_backend.entities.User;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public interface CartService {

    public CartDto getOrCreateCart(User user);

    public CartDto addProductToCart(User user, Integer productId, int quantity);

    public CartDto updateProductQuantityInCart(User user, Integer productId, int newQuantity);

    public void removeProductFromCart(User user, Integer productId);

    public void clearCart(User user);

    public Optional<CartDto> getCartByUser(User user);

    public BigDecimal getCartTotalPrice(User user);
}