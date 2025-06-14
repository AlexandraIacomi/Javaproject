package ro.foame.employee_backend.dtos;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemRequest {

    @NotNull(message = "Product ID must not be null")
    private Integer productId;

    @NotNull(message = "Quantity must not be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}