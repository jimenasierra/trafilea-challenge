package trafilea.challenge.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import trafilea.challenge.model.dto.ProductDto;
import trafilea.challenge.model.entity.Product;
import trafilea.challenge.persistence.ProductRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Map<Integer, Product> getProductsMapByIds(Set<Integer> ids) {
        List<Product> products = productRepository.findAllById(ids);
        return products.stream().collect(Collectors.toMap(Product::getId, Function.identity()));
    }

    public Product createProduct(ProductDto productDto) {
        Product product = Product.builder()
                .name(productDto.getName())
                .category(productDto.getCategory())
                .price(productDto.getPrice())
                .build();
        return productRepository.save(product);
    }
}
