package com.eshop.repositories.spring.jpa;

import com.eshop.models.entities.CartItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface JpaCartItemRepository extends JpaRepository<CartItem, String> {
    List<CartItem> findByCartId(String cartId, Pageable pageable);
}
