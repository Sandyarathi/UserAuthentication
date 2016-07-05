package com.ct.service;

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

	public UserService () {
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
	
	public User getUser() {
		//get User from Mongo
		UserDAO userDAO = new UserDAO();
		userDAO.setFirstName("Shailesh");
		userDAO.setLastName("Samudrala");
		userDAO.setUserName("shailesh");
		userDAO.setPassword("shailu");

		return generateUser(userDAO, null);
	}

	public User getAuthenticatedUser() {
		String userName = authHelper.getUsername();
		String token = createAuthToken();

		// get UserDAO from Mongo
		UserDAO userDAO = new UserDAO();
		userDAO.setFirstName("Sandhya");
		userDAO.setLastName("Das");
		userDAO.setUserName("sandhya");
		userDAO.setPassword("sandy");

		return generateUser(userDAO, token);

	}

	private User generateUser(UserDAO userDAO, String token) {
		User user = new User();
		user.setFirstName(userDAO.getFirstName());
		user.setLastName(userDAO.getLastName());
		user.setUserName(userDAO.getUserName());
		user.setToken(token);
		return user;
	}

}
