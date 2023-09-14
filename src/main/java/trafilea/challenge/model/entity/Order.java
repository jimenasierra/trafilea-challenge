package trafilea.challenge.model.entity;

import lombok.Data;

@Data
public class Order {
    private String cartId;
    private Double shipping;
    private Double discounts;
}
