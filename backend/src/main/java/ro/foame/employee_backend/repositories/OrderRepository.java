package ro.foame.employee_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ro.foame.employee_backend.entities.Order;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Optional<Order> findByPaypalOrderId(String paypalOrderId);
}
