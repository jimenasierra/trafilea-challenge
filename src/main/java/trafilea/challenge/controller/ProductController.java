package trafilea.challenge.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trafilea.challenge.exception.ApiException;
import trafilea.challenge.model.dto.ProductDto;
import trafilea.challenge.service.ProductService;;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity postProduct(@Valid @RequestBody ProductDto productDto) {

        log.info("Incoming request: POST Product - {} ", productDto);
        try {
            return new ResponseEntity<>(productService.createProduct(productDto), HttpStatus.OK);
        } catch (ApiException exception) {
            return new ResponseEntity<>(exception.getMessage(), exception.getHttpStatus());
        }
    }

    @GetMapping
    public ResponseEntity getProducts() {

        log.debug("Incoming request: GET Products");
        try {
            return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
        } catch (ApiException exception) {
            return new ResponseEntity<>(exception.getMessage(), exception.getHttpStatus());
        }
    }
}
