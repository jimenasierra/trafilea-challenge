package trafilea.challenge.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import trafilea.challenge.exception.ApiException;
import trafilea.challenge.model.dto.*;
import trafilea.challenge.model.entity.ProductCart;
import trafilea.challenge.service.CartService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity postCart(@Valid @RequestBody CartDto cart) {

        log.info("Incoming request: POST Cart - {} ", cart);
        try {
            return new ResponseEntity<>(cartService.createCart(cart), HttpStatus.OK);
        } catch (ApiException exception) {
            return new ResponseEntity<>(exception.getMessage(), exception.getHttpStatus());
        }
    }

    @GetMapping
    public ResponseEntity getCartByUserId(@RequestParam String userId) {

        log.debug("Incoming request: GET Cart by userId: {} ", userId);
        try {
            return new ResponseEntity<>(cartService.findCartByUserId(userId), HttpStatus.OK);
        } catch (ApiException exception) {
            return new ResponseEntity<>(exception.getMessage(), exception.getHttpStatus());
        }
    }


    @PostMapping("/product")
    public ResponseEntity postProductCart(@Valid @RequestBody PostProductRequestDto requestDtos) {

        log.info("Incoming request: POST add Products To Cart - {} ", requestDtos);
        List<ProductCart> productCarts = cartService.addProductsToCart(requestDtos.getUserId(), requestDtos.getProductCarts());
        try {
            return new ResponseEntity<>(productCarts, HttpStatus.OK);
        } catch (ApiException exception) {
            return new ResponseEntity<>(exception.getMessage(), exception.getHttpStatus());
        }
    }


    @PatchMapping("/product/{productId}")
    public ResponseEntity patchProductCart(@Valid @RequestBody PatchProductRequestDto requestDtos, @PathVariable Integer productId) {

        log.info("Incoming request: PATCH Modify product quantity - {} ", requestDtos);
        ProductCart productCarts = cartService.modifyProductCartQuantity(productId, requestDtos);
        try {
            return new ResponseEntity<>(productCarts, HttpStatus.OK);
        } catch (ApiException exception) {
            return new ResponseEntity<>(exception.getMessage(), exception.getHttpStatus());
        }
    }



    @GetMapping("/order")
    public ResponseEntity getOrder(@RequestParam String userId, @RequestParam Double shipping) {

        log.debug("Incoming request: GET Order from cart");
        try {
            return new ResponseEntity<>(cartService.createOrder(userId, shipping), HttpStatus.OK);
        } catch (ApiException exception) {
            return new ResponseEntity<>(exception.getMessage(), exception.getHttpStatus());
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
    }
}
