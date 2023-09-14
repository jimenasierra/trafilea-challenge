package trafilea.challenge.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import trafilea.challenge.TestObjectBuilder;
import trafilea.challenge.model.dto.*;
import trafilea.challenge.model.entity.Cart;
import trafilea.challenge.model.entity.ProductCart;
import trafilea.challenge.persistence.CartRepository;
import trafilea.challenge.persistence.ProductCartRepository;
import trafilea.challenge.util.enums.ProductCategory;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private ProductService productService;
    @Mock
    private ProductCartRepository productCartRepository;
    @InjectMocks
    private CartService cartService;
    private Cart cart;
    private List<ProductCart> productCarts;
    private ProductCart productCart;

    @Before
    public void setUp(){
        cart = TestObjectBuilder.buildCart();
        productCarts = TestObjectBuilder.buildProductCarts();
        productCart = TestObjectBuilder.buildProductCart(TestObjectBuilder.buildProduct(1, "product1", ProductCategory.COFFEE, 10D), 5);
        when(cartRepository.findByUserId(any())).thenReturn(Optional.ofNullable(cart));
        when(productService.getProductsMapByIds(any())).thenReturn(TestObjectBuilder.getProductMap());
        when(productCartRepository.saveAll(any())).thenReturn(productCarts);
        when(productCartRepository.findByCart_UserIdAndProduct_Id(any(), any())).thenReturn(Optional.ofNullable(productCart));
        when(productCartRepository.save(any())).thenReturn(productCart);
//        when(cartRepository.save(any())).thenReturn(Optional.ofNullable(cart));

    }

    @Test
    public void shouldCreateACartSuccessfully() {
        cart.setId(1);
        CartDto cartDto = CartDto.builder().userId("userId").build();

        Cart actualCart = cartService.createCart(cartDto);

        assertEquals(cart.getId(), actualCart.getId());
        assertEquals(cart.getUserId(), actualCart.getUserId());
    }

    @Test
    public void shouldAddProductsToTheCartSuccessfully() {
        List<ProductCartDto> productCartDtos = TestObjectBuilder.buildProductCartDto();
        List<ProductCart> productCarts = cartService.addProductsToCart("userId", productCartDtos);

        assertEquals(productCartDtos.size(), productCarts.size());
    }

    @Test
    public void shouldModifyProductQuantity() {
        PatchProductRequestDto requestDtos = PatchProductRequestDto.builder()
                .userId("userId")
                .quantity(10)
                .build();
        ProductCart productCart = cartService.modifyProductCartQuantity(1, requestDtos);

        assertEquals(requestDtos.getQuantity(), productCart.getQuantity());
    }

    @Test
    public void shouldCreateAnOrderFromCartWithFreeShippingAndDiscount() {
        cart.setProductCarts(productCarts);
        OrderDto order = cartService.createOrder("userId", 10D);
        TotalsDto totals = order.getTotals();

        double total= calculateTotal(productCarts);

        assertEquals("userId", order.getUserId());
        assertEquals(0D, totals.getShipping());
        assertEquals(0.1D, totals.getDiscounts());
        assertEquals(total, totals.getOrder());
    }

    private double calculateTotal(List<ProductCart> productCarts) {
        double productTotal = 0D;
        for (ProductCart productCart : productCarts) {
            productTotal += productCart.getProduct().getPrice() * productCart.getQuantity();
        }
        return productTotal - (productTotal * 0.1D);
    }
}