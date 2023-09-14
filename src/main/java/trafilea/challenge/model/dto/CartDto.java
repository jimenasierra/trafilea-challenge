package trafilea.challenge.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import trafilea.challenge.model.entity.Product;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartDto {
    @NotNull(message = "UserId must not be null")
    @NotBlank(message = "UserId must not be empty")
    private String userId;
    private List<Product> products;
}
