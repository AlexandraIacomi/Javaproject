package ro.foame.employee_backend.services.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
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

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper, OrderItemRepository orderItemRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.orderItemRepository = orderItemRepository;

        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        Order orderEntity = new Order();
        orderEntity.setCustomerName(orderDto.getCustomerName());
        orderEntity.setCustomerAddress(orderDto.getAddress());
        orderEntity.setCustomerPhone(orderDto.getPhoneNumber());
        orderEntity.setStatus("PENDING");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        orderEntity.setUser(user);

        Set<OrderItem> orderItems = new HashSet<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemDto itemDto : orderDto.getOrderItems()) {
            Product product = productRepository.findById(itemDto.getProductId().intValue())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

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
        return orderMapper.mapTo(savedOrder);
    }
}
