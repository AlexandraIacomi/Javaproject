package ro.foame.employee_backend.mappers.imp;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ro.foame.employee_backend.dtos.OrderDto;
import ro.foame.employee_backend.dtos.OrderItemDto;
import ro.foame.employee_backend.entities.Order;
import ro.foame.employee_backend.entities.OrderItem;
import ro.foame.employee_backend.mappers.Mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderMapper implements Mapper<Order, OrderDto> {

    private final ModelMapper modelMapper;
    private final OrderItemMapper orderItemMapper;

    OrderMapper(ModelMapper modelMapper, OrderItemMapper orderItemMapper) {
        this.modelMapper = modelMapper;
        this.orderItemMapper = orderItemMapper;
    }

    @Override
    public OrderDto mapTo(Order order) {
       OrderDto orderDto = modelMapper.map(order, OrderDto.class);
       if(order.getOrderItems() != null) {
           List<OrderItemDto> itemDtos = order.getOrderItems().stream()
                   .map(orderItemMapper::mapTo)
                   .collect(Collectors.toList());
           orderDto.setOrderItems(itemDtos);
       }
       return orderDto;
    }

    @Override
    public Order mapFrom(OrderDto orderDto) {
        Order order = modelMapper.map(orderDto, Order.class);
        if (orderDto.getOrderItems() != null) {
            Set<OrderItem> orderItems = orderDto.getOrderItems().stream()
                    .map(orderItemMapper::mapFrom)
                    .collect(Collectors.toSet());
            order.setOrderItems(orderItems);
        }
        return order;
    }

}
