package com.example.ARYAShopping.repositery;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ARYAShopping.entity.Orders;

public interface OrderRepositery extends JpaRepository<Orders,Integer>{

}
