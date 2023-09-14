package trafilea.challenge.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import trafilea.challenge.model.entity.Cart;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    Optional<Cart> findByUserId(String userId);

}
