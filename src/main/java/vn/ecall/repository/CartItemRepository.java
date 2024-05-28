package vn.ecall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ecall.domain.businessmodel.CartItem;

@Repository
public interface CartItemRepository  extends JpaRepository<CartItem, Integer> {
}
