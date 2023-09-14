package trafilea.challenge.model.dto;

import lombok.Data;

@Data
public class TotalsDto {

    public TotalsDto(Double shipping) {
        this.order = 0D;
        this.products = 0D;
        this.discounts = 0D;
        this.shipping = shipping;
    }

    private Double products;
    private Double discounts;
    private Double shipping;
    private Double order;
}
