package ro.foame.employee_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;
    private String name;
    private String imageUrl;
    private BigDecimal price;
    private String description;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String slug;
    private Set<CategoryDto> categories;
}
