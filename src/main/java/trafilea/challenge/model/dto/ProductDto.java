package trafilea.challenge.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import trafilea.challenge.util.enums.ProductCategory;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
    private Integer id;

    @NotNull(message = "Product name must not be null")
    @NotBlank(message = "Product name must not be empty")
    private String name;
    private ProductCategory category;
    @Min(0)
    private Double price;
}
