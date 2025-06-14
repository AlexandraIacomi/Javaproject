package ro.foame.employee_backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.foame.employee_backend.dtos.OrderDto;
import ro.foame.employee_backend.services.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) {
        try{
            OrderDto createdOrder = orderService.createOrder(orderDto);
            return ResponseEntity.ok(createdOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
