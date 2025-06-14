package ro.foame.employee_backend.mappers.imp;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import ro.foame.employee_backend.dtos.CartItemDto;
import ro.foame.employee_backend.entities.CartItem;
import ro.foame.employee_backend.mappers.Mapper;
@Component
public class CartItemMapper implements Mapper<CartItem, CartItemDto> {
    private final ModelMapper modelMapper;
    private final ProductMapper productMapper;

    public CartItemMapper(ModelMapper modelMapper, ProductMapper productMapper) {
        this.modelMapper = modelMapper;
        this.productMapper = productMapper;
    }

    @Override
    public CartItemDto mapTo(CartItem cartItem) {
        CartItemDto dto = modelMapper.map(cartItem, CartItemDto.class);
        if (cartItem.getProduct() != null) {
            dto.setProduct(productMapper.mapTo(cartItem.getProduct()));
        }
        dto.setSubtotal(cartItem.getSubtotal());
        return dto;
    }

    @Override
    public CartItem mapFrom(CartItemDto cartItemDTO) {
        CartItem entity = modelMapper.map(cartItemDTO, CartItem.class);
        if (cartItemDTO.getProduct() != null) {
            entity.setProduct(productMapper.mapFrom(cartItemDTO.getProduct()));
        }
        return entity;
    }
}
