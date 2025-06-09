package ro.foame.employee_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ProductCreateDto {
    private String name;
    private String imageUrl;
    private BigDecimal price;
    private String description;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String slug;
    private List<Integer> categoriesIds;
}
