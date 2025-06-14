package ro.foame.employee_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.foame.employee_backend.entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

}
