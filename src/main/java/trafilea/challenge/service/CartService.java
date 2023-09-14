package trafilea.challenge.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import trafilea.challenge.exception.NotFoundException;
import trafilea.challenge.model.dto.*;
import trafilea.challenge.model.entity.Cart;
import trafilea.challenge.model.entity.Product;
import trafilea.challenge.model.entity.ProductCart;
import trafilea.challenge.persistence.CartRepository;
import trafilea.challenge.persistence.ProductCartRepository;
import trafilea.challenge.util.enums.ProductCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    private final ProductCartRepository productCartRepository;
    private final ProductService productService;

    public Cart createCart(CartDto cart) {

        // If already exists a Cart for an userId we return it. Otherwise, we create a new one
        Optional<Cart> cartOptional = cartRepository.findByUserId(cart.getUserId());
        return cartOptional.orElseGet(() -> cartRepository.save(Cart.builder().userId(cart.getUserId()).build()));
    }

    public Cart findCartByUserId(String userId) {
        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
        if (cartOptional.isEmpty()) {
            throw new NotFoundException("Cart with userId " + userId + " not found");
        }
        return cartOptional.get();
    }

    public List<ProductCart> addProductsToCart(String userId, List<ProductCartDto> productCartDtos) {
        Cart cart = findCartByUserId(userId);
        Map<Integer, Product> productsMap = productService.getProductsMapByIds(productCartDtos.stream()
                .map(ProductCartDto::getProductId)
                .collect(Collectors.toSet()));

        List<ProductCart> productCarts = new ArrayList<>();
        productCartDtos.forEach(product -> {
            ProductCart p = ProductCart.builder()
                    .cart(cart)
                    .product(productsMap.get(product.getProductId()))
                    .quantity(product.getQuantity())
                    .build();
            productCarts.add(p);
        });

        return productCartRepository.saveAll(productCarts);
    }

    public ProductCart modifyProductCartQuantity(Integer productId, PatchProductRequestDto requestDtos) {
        ProductCart productCart = findProductCartByUserIdAndProductId(requestDtos.getUserId(), productId);
        productCart.setQuantity(requestDtos.getQuantity());
        return productCartRepository.save(productCart);
    }

    private ProductCart findProductCartByUserIdAndProductId(String userId, Integer productId) {
        Optional<ProductCart> productCartOptional = productCartRepository.findByCart_UserIdAndProduct_Id(userId, productId);
        if (productCartOptional.isEmpty()) {
            throw new NotFoundException("Product " + productId + " not found in the cart");
        }
        return productCartOptional.get();
    }

    public OrderDto createOrder(String userId, Double shipping) {
        Cart cart = findCartByUserId(userId);
        List<ProductCart> productCarts = cart.getProductCarts();
        TotalsDto totals = new TotalsDto(shipping);

        validatesCoffeeCategory(productCarts, totals);
        validatesEquipmentCategory(productCarts, totals);
        validatesAccessoriesCategory(productCarts, totals);
        calculateOrderTotal(productCarts, totals);
        return OrderDto.builder().userId(userId).totals(totals).build();
    }

    private void calculateOrderTotal(List<ProductCart> productCarts, TotalsDto totals) {
        double productTotal = 0D;
        for (ProductCart productCart : productCarts) {
            productTotal += productCart.getProduct().getPrice() * productCart.getQuantity();
        }
        Double total = productTotal - (productTotal * totals.getDiscounts()) + totals.getShipping();
        totals.setProducts(productTotal);
        totals.setOrder(total);
    }

    private void validatesCoffeeCategory(List<ProductCart> productCarts, TotalsDto totals) {
        int count = getProductsByCategory(ProductCategory.COFFEE, productCarts);
        if (count >= 2) {
            addFreeCoffee(productCarts);
        }
    }

    private void validatesEquipmentCategory(List<ProductCart> productCarts, TotalsDto totals) {
        int count = getProductsByCategory(ProductCategory.EQUIPMENT, productCarts);
        if (count > 3) {
            totals.setShipping(0D);
        }
    }

    private void validatesAccessoriesCategory(List<ProductCart> productCarts, TotalsDto totals) {
        Double totalAccessories = 0D;
        for (ProductCart productCart : productCarts) {
            if (ProductCategory.ACCESSORIES.equals(productCart.getProduct().getCategory())) {
                totalAccessories += productCart.getProduct().getPrice() * productCart.getQuantity();
            }
        }
        if (totalAccessories > 70) {
            totals.setDiscounts(totals.getDiscounts() + 0.1);
        }
    }

    private int getProductsByCategory(ProductCategory category, List<ProductCart> productCarts) {
        int count = 0;
        for (ProductCart productCart : productCarts) {
            if (ProductCategory.COFFEE.equals(productCart.getProduct().getCategory())) {
                count += productCart.getQuantity();
            }
        }
        return count;
    }

    private void addFreeCoffee(List<ProductCart> productCarts) {
        // This product is symbolic
        Product product = Product.builder()
                .name("Free Coffee")
                .price(0D)
                .build();
        Cart cart = productCarts.get(0).getCart();
        ProductCart freeCoffee = ProductCart.builder().product(product).cart(cart).quantity(1).build();
        productCarts.add(freeCoffee);
    }
}
