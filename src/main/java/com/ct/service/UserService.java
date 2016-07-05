package com.ct.service;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ct.dao.UserDAO;
import com.ct.model.User;

@Service
public class UserService {

	   private BCryptPasswordEncoder passwordEncoder;
	   public static HashMap<String,UserDAO> userList= new HashMap<String,UserDAO>();

	public UserService() { 
		 passwordEncoder = new BCryptPasswordEncoder();
	}
	@Autowired
	private AuthHelper authHelper;

	private String createAuthToken() {
		String token = Base64.encodeBase64String(UUID.randomUUID().toString()
				.getBytes());
		authHelper.saveToken(token);
		return token;
	}

	public User getAuthenticatedUser() {
		String userName = authHelper.getUsername();
		String token = createAuthToken();
		UserDAO userFromStoredList=userList.get(userName);
		User user=new User();
		user.setFirstName(userFromStoredList.getFirstName());
		user.setLastName(userFromStoredList.getLastName());
		user.setUserName(userFromStoredList.getUserName());
		user.setToken(token);
		return user;

	}

	
	public User createUser(UserDAO newUserDAO) {
		//int generatedId=generateID();
		userList.put(newUserDAO.getUserName(), newUserDAO);
		User user=new User();
		user.setFirstName(newUserDAO.getFirstName());
		user.setLastName(newUserDAO.getLastName());
		user.setUserName(newUserDAO.getUserName());
		user.setToken(null);
		return user;
	}

}
