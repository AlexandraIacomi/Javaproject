package ro.foame.employee_backend.mappers.imp;


import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import ro.foame.employee_backend.dtos.CartDto;
import ro.foame.employee_backend.dtos.CartItemDto;

import ro.foame.employee_backend.entities.Cart;
import ro.foame.employee_backend.mappers.Mapper;
import ro.foame.employee_backend.mappers.imp.CartItemMapper;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper implements Mapper<Cart, CartDto> {
    private final ModelMapper modelMapper;
    private final UserMapper userMapper;
    private final CartItemMapper cartItemMapper;

    public CartMapper(ModelMapper modelMapper, UserMapper userMapper, CartItemMapper cartItemMapper) {
        this.modelMapper = modelMapper;
        this.userMapper = userMapper;
        this.cartItemMapper = cartItemMapper;
    }

    @Override
    public CartDto mapTo(Cart cart) {
        CartDto dto = modelMapper.map(cart, CartDto.class);

        if (cart.getUser() != null) {
            dto.setUser(userMapper.mapTo(cart.getUser()));
        }

        if (cart.getCartItems() != null) {
            List<CartItemDto> itemDTOs = cart.getCartItems().stream()
                    .map(cartItemMapper::mapTo)
                    .collect(Collectors.toList());
            dto.setItems(itemDTOs);
        }

        dto.setTotalPrice(cart.getTotalPrice());
        return dto;
    }

    @Override
    public Cart mapFrom(CartDto cartDTO) {
        Cart entity = modelMapper.map(cartDTO, Cart.class);


        if (cartDTO.getUser() != null) {
            entity.setUser(userMapper.mapFrom(cartDTO.getUser()));
        }

        return entity;
    }
}