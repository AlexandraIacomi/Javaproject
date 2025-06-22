package ro.foame.employee_backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/paypal/success")
    public ResponseEntity<String> captureOrder(@RequestParam String token, @RequestParam Integer orderId) {
        try {
            String result = orderService.captureOrder(token, orderId);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Payment capture failed.");
        }
    }

    @GetMapping("/paypal/cancel")
    public ResponseEntity<String> cancelOrder(@RequestParam Integer orderId) {
        try {
            String result = orderService.handleCancel(orderId);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Cancellation failed.");
        }
    }

    @PostMapping("/paypal/confirm")
    public ResponseEntity<String> confirmPayPalPayment(@RequestParam String paypalOrderId, @RequestParam Integer orderId) {
        try {
            String result = orderService.confirmPayPalPayment(paypalOrderId, orderId);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Payment confirmation failed: " + e.getMessage());
        }
    }
}
