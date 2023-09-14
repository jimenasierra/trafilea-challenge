package trafilea.challenge.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PostProductRequestDto {
    @NotNull(message = "UserId must not be null")
    @NotBlank(message = "UserId must not be empty")
    private String userId;
    private List<ProductCartDto> productCarts;
}
