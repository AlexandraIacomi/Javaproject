package ro.foame.employee_backend.controllers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ro.foame.employee_backend.dtos.CartDto;
import ro.foame.employee_backend.dtos.CartItemRequest;
import ro.foame.employee_backend.entities.User;
import ro.foame.employee_backend.services.CartService;

import java.math.BigDecimal;


@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartDto> getCart(@AuthenticationPrincipal User user) {
        CartDto cart = cartService.getOrCreateCart(user);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    public ResponseEntity<CartDto> addProductToCart(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CartItemRequest cartItemRequest) {
        CartDto updatedCart = cartService.addProductToCart(user, cartItemRequest.getProductId(), cartItemRequest.getQuantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedCart);
    }

    @PutMapping("/update")
    public ResponseEntity<CartDto> updateProductQuantityInCart(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CartItemRequest cartItemRequest) {
        CartDto updatedCart = cartService.updateProductQuantityInCart(user, cartItemRequest.getProductId(), cartItemRequest.getQuantity());
        return ResponseEntity.ok(updatedCart);
    }


    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Void> removeProductFromCart(
            @AuthenticationPrincipal User user,
            @PathVariable Integer productId) {
        cartService.removeProductFromCart(user, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal User user) {
        cartService.clearCart(user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/total")
    public ResponseEntity<BigDecimal> getCartTotalPrice(@AuthenticationPrincipal User user) {
        BigDecimal totalPrice = cartService.getCartTotalPrice(user);
        return ResponseEntity.ok(totalPrice);
    }
}