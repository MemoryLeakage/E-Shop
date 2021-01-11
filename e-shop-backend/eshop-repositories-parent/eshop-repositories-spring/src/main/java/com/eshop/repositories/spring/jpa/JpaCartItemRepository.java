package com.eshop.repositories.spring.jpa;

import com.eshop.models.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface JpaCartItemRepository extends JpaRepository<CartItem, String> {
}
