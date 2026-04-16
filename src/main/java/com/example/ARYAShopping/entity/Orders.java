package com.example.ARYAShopping.entity;


import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")  // table name in database
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private int quantity;
    public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	private LocalDate date;

    @Override
	public String toString() {
		return "Orders [id=" + id + ", productName=" + productName + ", quantity=" + quantity + ", user=" + user + "]";
	}
	// Many Orders belong to one User
    @ManyToOne
    @JoinColumn(name = "user_mobile")  // foreign key column
    private User user;

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }



    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}