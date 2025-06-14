package ro.foame.employee_backend.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.foame.employee_backend.dtos.CartDto;
import ro.foame.employee_backend.entities.Cart;
import ro.foame.employee_backend.entities.CartItem;
import ro.foame.employee_backend.entities.Product;
import ro.foame.employee_backend.entities.User;
import ro.foame.employee_backend.mappers.imp.CartItemMapper;
import ro.foame.employee_backend.mappers.imp.CartMapper;
import ro.foame.employee_backend.repositories.CartItemRepository;
import ro.foame.employee_backend.repositories.CartRepository;
import ro.foame.employee_backend.repositories.ProductRepository;
import ro.foame.employee_backend.repositories.UserRepository;
import ro.foame.employee_backend.services.CartService;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;


    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductRepository productRepository, UserRepository userRepository, CartMapper cartMapper, CartItemMapper cartItemMapper) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.cartMapper = cartMapper;
    }

    @Override
    @Transactional
    public CartDto getOrCreateCart(User user) {
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder().user(user).build();
                    return cartRepository.save(newCart);
                });
        return cartMapper.mapTo(cart);
    }

    @Override
    @Transactional
    public CartDto addProductToCart(User user, Integer productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive to add a product.");
        }

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder().user(user).build();
                    return cartRepository.save(newCart);
                });

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        Optional<CartItem> existingCartItem = cartItemRepository.findByCartAndProduct(cart, product);

        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
        } else {
            CartItem newCartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .build();
            cartItemRepository.save(newCartItem);
        }
        return cartMapper.mapTo(cart);
    }

    @Override
    @Transactional
    public CartDto updateProductQuantityInCart(User user, Integer productId, int newQuantity) {

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user: " + user.getUsername()));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new IllegalArgumentException("Product with ID " + productId + " not found in cart."));

        if (newQuantity <= 0) {
            cart.getCartItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(newQuantity);
            cartItemRepository.save(cartItem);
        }
        return cartMapper.mapTo(cart);
    }

    @Override
    @Transactional
    public void removeProductFromCart(User user, Integer productId) {
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user: " + user.getUsername()));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new IllegalArgumentException("Product with ID " + productId + " not found in cart."));

        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
    }

    @Override
    public Optional<CartDto> getCartByUser(User user) {
        return cartRepository.findByUser(user)
                .map(cartMapper::mapTo);
    }


    @Override
    public BigDecimal getCartTotalPrice(User user) {
        return cartRepository.findByUser(user)
                .map(Cart::getTotalPrice)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    @Transactional
    public void clearCart(User user) {
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user: " + user.getUsername()));
        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

}



