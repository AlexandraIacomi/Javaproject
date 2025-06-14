package ro.foame.employee_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private String customerName;
    private String address;
    private String phoneNumber;
    private List<OrderItemDto> orderItems;
}
