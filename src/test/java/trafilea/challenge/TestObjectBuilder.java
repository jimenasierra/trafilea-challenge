package trafilea.challenge;

import trafilea.challenge.model.dto.ProductCartDto;
import trafilea.challenge.model.entity.Cart;
import trafilea.challenge.model.entity.Product;
import trafilea.challenge.model.entity.ProductCart;
import trafilea.challenge.util.enums.ProductCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestObjectBuilder {

    public static Cart buildCart() {
        return Cart.builder()
                .userId("userId")
                .build();
    }

    public static List<ProductCartDto> buildProductCartDto() {
        ProductCartDto productCartDto1 = ProductCartDto.builder()
                .productId(1)
                .quantity(5)
                .build();
        ProductCartDto productCartDto2 = ProductCartDto.builder()
                .productId(2)
                .quantity(3)
                .build();
        ProductCartDto productCartDto3 = ProductCartDto.builder()
                .productId(3)
                .quantity(3)
                .build();
        return List.of(productCartDto1, productCartDto2, productCartDto3);
    }

    public static Product buildProduct(Integer id, String name, ProductCategory category, Double price) {
        return Product.builder()
                .id(id)
                .name(name)
                .price(price)
                .category(category)
                .build();
    }

    public static Map<Integer, Product> getProductMap() {
        Product product1 = buildProduct(1, "product1", ProductCategory.COFFEE, 10D);
        Product product2 = buildProduct(2, "product2", ProductCategory.EQUIPMENT, 15D);
        Product product3 = buildProduct(3, "product3", ProductCategory.ACCESSORIES, 25D);
        return Map.of(1, product1,
                    2, product2,
                    3, product3);
    }

    public static List<ProductCart> buildProductCarts() {
        ProductCart productCart1 = buildProductCart(buildProduct(1, "product1", ProductCategory.COFFEE, 10D), 5);
        ProductCart productCart2 = ProductCart.builder()
                .product(buildProduct(2, "product2", ProductCategory.EQUIPMENT, 15D))
                .quantity(3)
                .build();
        ProductCart productCart3 = ProductCart.builder()
                .product(buildProduct(3, "product3", ProductCategory.ACCESSORIES, 25D))
                .quantity(3)
                .build();

        List<ProductCart> list = new ArrayList<>();
        list.add(productCart1);
        list.add(productCart2);
        list.add(productCart3);
        return list;
    }

    public static ProductCart buildProductCart(Product product, int quantity) {
        return ProductCart.builder()
                .product(product)
                .quantity(quantity)
                .build();
    }
}
