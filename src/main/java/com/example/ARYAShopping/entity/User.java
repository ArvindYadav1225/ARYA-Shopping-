package com.example.ARYAShopping.entity;
import jakarta.persistence.CollectionTable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Entity
public class User {
	@NotBlank(message="Name is Required.")

	String name;
	@Email(message="Invalid Email")
	String gmail;
	public String getGmail() {
		return gmail;
	}
	public void setGmail(String gmail) {
		this.gmail = gmail;
	}
	public List<Orders> getOrders() {
		return orders;
	}
	public void setOrders(List<Orders> orders) {
		this.orders = orders;
	}
	public List<SearchHistory> getSearchHistories() {
		return searchHistories;
	}
	public void setSearchHistories(List<SearchHistory> searchHistories) {
		this.searchHistories = searchHistories;
	}
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getProfileImage() {
		return profileImage;
	}
	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	public List<Orders> getOrder() {
		return orders;
	}
	public void setOrder(List<Orders> order) {
		this.orders = order;
	}
	public List<Product> getSeen() {
		return seen;
	}
	public void setSeen(List<Product> seen) {
		this.seen = seen;
	}
	public String getUpiDetails() {
		return upiDetails;
	}
	public void setUpiDetails(String upiDetails) {
		this.upiDetails = upiDetails;
	}
	public List<SearchHistory> getSearchhistory() {
		return searchHistories;
	}
	public void setSearchhistory(List<SearchHistory> searchhistory) {
		this.searchHistories = searchhistory;
	}
	public List<Product> getProcessing() {
		return processing;
	}
	public void setProcessing(List<Product> processing) {
		this.processing = processing;
	}
	@Id
	
	@Column(unique=true)
	String mobile;
	String profileImage;
	@NotBlank(message="password cann,t be blank")
	
	
	String password;
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	String location;
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<CartItems> cartItems = new ArrayList<>();
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Orders> orders=new ArrayList<>();
	@OneToMany(mappedBy = "user",
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	
	public List<CartItems> getCartItems() {
		return cartItems;
	}
	public void setCartItems(List<CartItems> cartItems) {
		this.cartItems = cartItems;
	}
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	List<Product> seen;
	
	String upiDetails;
	 @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	    private List<SearchHistory> searchHistories = new ArrayList<>();
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	List<Product>processing;
	
	

	
	

}
