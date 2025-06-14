package ro.foame.employee_backend.services;

import org.springframework.stereotype.Service;
import ro.foame.employee_backend.dtos.OrderDto;

@Service
public interface OrderService {

    public OrderDto createOrder(OrderDto order);
}
