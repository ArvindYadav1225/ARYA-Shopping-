package com.example.ARYAShopping.repositery;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ARYAShopping.entity.SearchHistory;
import com.example.ARYAShopping.entity.User;

public interface SearchHistoryRepository
extends JpaRepository<SearchHistory, Long> {

	List<SearchHistory> findTop10ByUserOrderByCreatedAtDesc(User user);

}

