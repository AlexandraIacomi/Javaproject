package ro.foame.employee_backend.mappers.imp;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ro.foame.employee_backend.dtos.OrderItemDto;
import ro.foame.employee_backend.entities.OrderItem;
import ro.foame.employee_backend.mappers.Mapper;

@Component
public class OrderItemMapper implements Mapper<OrderItem, OrderItemDto> {
    private final ModelMapper modelMapper;

    public OrderItemMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderItemDto mapTo(OrderItem orderItem) {
        return modelMapper.map(orderItem, OrderItemDto.class);
    }

    @Override
    public OrderItem mapFrom(OrderItemDto orderItemDto) {
        return modelMapper.map(orderItemDto, OrderItem.class);
    }
}
