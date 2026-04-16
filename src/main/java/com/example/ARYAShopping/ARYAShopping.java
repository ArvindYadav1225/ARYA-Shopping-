package com.example.ARYAShopping;



import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.ARYAShopping.entity.Product;
import com.example.ARYAShopping.repositery.ProductRepositery;

@SpringBootApplication
public class ARYAShopping implements CommandLineRunner{
	@Autowired
	private ProductRepositery pr;
	public static void main(String[] args) {
		SpringApplication.run(ARYAShopping.class, args);
		
	}

	@Override
	public void run(String... args) throws Exception {
	
	}

}
