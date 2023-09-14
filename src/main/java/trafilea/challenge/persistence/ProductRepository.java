package trafilea.challenge.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import trafilea.challenge.model.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
