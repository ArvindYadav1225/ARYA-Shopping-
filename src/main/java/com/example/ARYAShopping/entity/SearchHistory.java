package com.example.ARYAShopping.entity;


import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "search_history")
public class SearchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "search_text", length = 255, nullable = false)
    private String searchText;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public SearchHistory() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SearchHistory(Long id, String searchText, LocalDateTime createdAt, User user) {
		super();
		this.id = id;
		this.searchText = searchText;
		this.createdAt = createdAt;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    // getters & setters
}

