package ro.foame.employee_backend.dtos;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {
    private Long id;
    private UserDto user;
    private List<CartItemDto> items;
    private BigDecimal totalPrice;
}
