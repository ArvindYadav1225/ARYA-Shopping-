package com.example.ARYAShopping.repositery;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ARYAShopping.entity.Product;

public interface ProductRepositery extends JpaRepository<Product,Integer> {
	Product getByName(String name);

}
