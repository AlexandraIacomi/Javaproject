package ro.foame.employee_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.foame.employee_backend.entities.Cart;
import ro.foame.employee_backend.entities.CartItem;
import ro.foame.employee_backend.entities.Product;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}