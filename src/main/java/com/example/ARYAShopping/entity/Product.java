package com.example.ARYAShopping.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OneToMany;


@Entity
public class Product {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int id;
	
	String name;
	
	String model;
    public Product() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Product(int id, String name, String model, double price, String about, String image, String description,
			String discount, List<String> productCategory) {
		super();
		this.id = id;
		this.name = name;
		this.model = model;
		this.price = price;
		
		this.about = about;
		this.image = image;
		this.description = description;
		this.discount = discount;
		this.productCategory = productCategory;
	}
	public Product(int id, String name, String model, double price, double oldprice, String about, String image,
			String description, String discount, User user, List<String> productCategory) {
		super();
		this.id = id;
		this.name = name;
		this.model = model;
		this.price = price;
		this.oldprice = oldprice;
		this.about = about;
		this.image = image;
		this.description = description;
		this.discount = discount;
		this.user = user;
		this.productCategory = productCategory;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", model=" + model + ", price=" + price + ", oldprice="
				+ oldprice + ", about=" + about + ", image=" + image + ", description=" + description + ", discount="
				+ discount + ", productCategory=" + productCategory + ", getId()=" + getId() + ", getName()="
				+ getName() + ", getModel()=" + getModel() + ", getPrice()=" + getPrice() + ", getAbout()=" + getAbout()
				+ ", getImage()=" + getImage() + ", getDescription()=" + getDescription() + ", getDiscount()="
				+ getDiscount() + ", getProductCategory()=" + getProductCategory() + ", getOldprice()=" + getOldprice()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public List<String> getProductCategory() {
		return productCategory;
	}
	public void setProductCategory(List<String> productCategory) {
		this.productCategory = productCategory;
	}
	double price;
	double oldprice;
    public Product(int id, String name, String model, double price, double oldprice, String about, String image,
			String description, String discount, List<String> productCategory) {
		super();
		this.id = id;
		this.name = name;
		this.model = model;
		this.price = price;
		this.oldprice = oldprice;
		this.about = about;
		this.image = image;
		this.description = description;
		this.discount = discount;
		this.productCategory = productCategory;
	}
	
	
	
	
	

	public double getOldprice() {
		return oldprice;
	}

	public void setOldprice(double oldprice) {
		this.oldprice = oldprice;
	}
	String about;
	
	String image;
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
	    name = "product_attributes",
	    joinColumns = @JoinColumn(name = "product_id")
	)
	@MapKeyColumn(name = "attribute_name")
	@Column(name = "attribute_value")
     Map<String, String> hilights = new HashMap<>();
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
	    name = "product_attributes2",
	    joinColumns = @JoinColumn(name = "product_id2")
	)
	@MapKeyColumn(name = "attribute_name2")
	@Column(name = "attribute_value2")
     Map<String, String> otherDetails = new HashMap<>();
	
    public Map<String, String> getOtherDetails() {
		return otherDetails;
	}
	public void setOtherDetails(Map<String, String> otherDetails) {
		this.otherDetails = otherDetails;
	}
	String description;
    String discount;
    public Map<String, String> getHilights() {
		return hilights;
	}
	public void setHilights(Map<String, String> attributes) {
		this.hilights = attributes;
	}
	@ManyToOne
	 User user;
    List<String>productCategory;
   
    

}
