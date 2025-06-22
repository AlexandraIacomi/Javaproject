package ro.foame.employee_backend.services.impl;

import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ro.foame.employee_backend.controllers.client.PayPalClient;
import ro.foame.employee_backend.dtos.OrderDto;
import ro.foame.employee_backend.dtos.OrderItemDto;
import ro.foame.employee_backend.entities.Order;
import ro.foame.employee_backend.entities.OrderItem;
import ro.foame.employee_backend.entities.Product;
import ro.foame.employee_backend.entities.User;
import ro.foame.employee_backend.mappers.imp.OrderMapper;
import ro.foame.employee_backend.repositories.OrderItemRepository;
import ro.foame.employee_backend.repositories.OrderRepository;
import ro.foame.employee_backend.repositories.ProductRepository;
import ro.foame.employee_backend.repositories.UserRepository;
import ro.foame.employee_backend.services.OrderService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PayPalClient payPalClient;

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper, OrderItemRepository orderItemRepository, ProductRepository productRepository, UserRepository userRepository, PayPalClient payPalClient) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.payPalClient = payPalClient;
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) {

        Order orderEntity = new Order();
        orderEntity.setCustomerName(orderDto.getCustomerName());
        orderEntity.setCustomerAddress(orderDto.getAddress());
        orderEntity.setCustomerPhone(orderDto.getPhoneNumber());
        orderEntity.setStatus("PENDING_PAYMENT");
        orderEntity.setPaymentStatus("PENDING");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    return new RuntimeException("User not found");
                });
        orderEntity.setUser(user);

        Set<OrderItem> orderItems = new HashSet<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemDto itemDto : orderDto.getOrderItems()) {
            Product product = productRepository.findById(itemDto.getProductId().intValue())
                    .orElseThrow(() -> {
                        return new RuntimeException("Product not found");
                    });

            BigDecimal price = product.getPrice();
            Integer quantity = itemDto.getQuantity();
            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(quantity));

            OrderItem orderItem = OrderItem.builder()
                    .order(orderEntity)
                    .product(product)
                    .price(price)
                    .quantity(quantity)
                    .subtotal(subtotal)
                    .build();

            orderItems.add(orderItem);
            total = total.add(subtotal);
        }

        orderEntity.setOrderItems(orderItems);
        orderEntity.setTotalPrice(total);

        Order savedOrder = orderRepository.save(orderEntity);


        PayPalHttpClient client = payPalClient.client();
        OrderRequest request = new OrderRequest();
        request.checkoutPaymentIntent("CAPTURE");

        ApplicationContext applicationContext = new ApplicationContext()
                .returnUrl("http://localhost:4200/order-success?orderId=" + savedOrder.getId())
                .cancelUrl("http://localhost:4200/order-cancel?orderId=" + savedOrder.getId());

        AmountWithBreakdown amount = new AmountWithBreakdown()
                .currencyCode("USD")
                .value(total.toString());

        PurchaseUnitRequest purchaseUnit = new PurchaseUnitRequest()
                .referenceId(savedOrder.getId().toString())
                .amountWithBreakdown(amount);

        request.purchaseUnits(List.of(purchaseUnit));
        request.applicationContext(applicationContext);

        OrdersCreateRequest createRequest = new OrdersCreateRequest().requestBody(request);

        try {
            HttpResponse<com.paypal.orders.Order> response = client.execute(createRequest);
            String paypalOrderId = response.result().id();
            String paypalOrderStatus = response.result().status();


            savedOrder.setPaypalOrderId(paypalOrderId);

            if ("CREATED".equalsIgnoreCase(paypalOrderStatus) || "SAVED".equalsIgnoreCase(paypalOrderStatus)) {
                savedOrder.setPaymentStatus("PENDING_PAYPAL_APPROVAL");
                savedOrder.setStatus("PAYMENT_INITIATED");

            } else if ("COMPLETED".equalsIgnoreCase(paypalOrderStatus)) {
                savedOrder.setPaymentStatus("COMPLETED");
                savedOrder.setStatus("PAID");
            }
            orderRepository.save(savedOrder);

            OrderDto responseDto = orderMapper.mapTo(savedOrder);
            responseDto.setPaypalOrderId(paypalOrderId);
            responseDto.setStatus(savedOrder.getStatus());
            return responseDto;

        } catch (IOException e) {
            e.printStackTrace();

            savedOrder.setPaymentStatus("FAILED_INITIATION");
            savedOrder.setStatus("PAYMENT_FAILED");
            orderRepository.save(savedOrder);

            throw new RuntimeException("Failed to create PayPal order: " + e.getMessage(), e);
        }
    }

    @Override
    public String captureOrder(String paypalToken, Integer orderId) {
        PayPalHttpClient client = payPalClient.client();

        OrdersCaptureRequest request = new OrdersCaptureRequest(paypalToken);
        try {
            HttpResponse<com.paypal.orders.Order> response = client.execute(request);
            String status = response.result().status();

            if ("COMPLETED".equalsIgnoreCase(status)) {
                Order order = orderRepository.findById(orderId)
                        .orElseThrow(() -> new RuntimeException("Order not found"));

                order.setPaymentStatus("COMPLETED");
                order.setStatus("PAID");
                orderRepository.save(order);

                return "Order captured successfully.";
            } else {

                Order order = orderRepository.findById(orderId)
                        .orElseThrow(() -> new RuntimeException("Order not found"));
                order.setPaymentStatus(status);
                orderRepository.save(order);
                return "Payment not completed. Status: " + status;
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to capture PayPal order", e);
        }
    }

    @Override
    public String handleCancel(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setPaymentStatus("CANCELLED");
        order.setStatus("CANCELLED");
        orderRepository.save(order);

        return "Order cancelled successfully.";
    }


    @Override
    public String confirmPayPalPayment(String paypalOrderId, Integer localOrderId) {


        Order order = orderRepository.findById(localOrderId)
                .orElseThrow(() -> {
                    return new RuntimeException("Local order not found with ID: " + localOrderId);
                });

        order.setPaymentStatus("COMPLETED");
        order.setStatus("PAID");
        orderRepository.save(order);

        return "Payment confirmed successfully.";
    }
}