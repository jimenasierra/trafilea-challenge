package trafilea.challenge.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductCartDto {
    @Valid
    @NotNull(message = "Product should not be null")
    private Integer productId;
    @Min(value = 0, message = "Quantity should at least 0")
    private Integer quantity;
}
