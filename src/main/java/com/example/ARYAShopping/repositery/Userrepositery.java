package com.example.ARYAShopping.repositery;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ARYAShopping.entity.User;

public interface Userrepositery extends JpaRepository<User,String> {
	public User getByMobile(String mobile);
	public User getByGmail(String gmail);

}
