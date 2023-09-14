package trafilea.challenge.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PatchProductRequestDto {
    @NotNull(message = "UserId must not be null")
    @NotBlank(message = "UserId must not be empty")
    private String userId;
    @Min(0)
    private Integer quantity;
}
