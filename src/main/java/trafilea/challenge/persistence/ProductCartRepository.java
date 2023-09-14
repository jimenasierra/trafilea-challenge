package trafilea.challenge.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import trafilea.challenge.model.entity.ProductCart;

import java.util.List;
import java.util.Optional;

public interface ProductCartRepository extends JpaRepository<ProductCart, Integer> {
    Optional<ProductCart> findByCart_UserIdAndProduct_Id(String userId, Integer productId);
}
