package com.example.ARYAShopping.repositery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ARYAShopping.entity.CartItems;
import com.example.ARYAShopping.entity.User;

import jakarta.transaction.Transactional;

public interface CartRepository extends JpaRepository<CartItems,Integer> {
	@Modifying
	@Transactional
	@Query("DELETE FROM CartItems c WHERE c.user = :user AND c.product.id = :productId")
	void deleteByUserAndProductId(@Param("user") User user,
	                              @Param("productId") int productId);

	void deleteByUserAndProductName(User u, String name);
	
}
