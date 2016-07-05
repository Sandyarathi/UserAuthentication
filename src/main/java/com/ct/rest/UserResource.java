package com.ct.rest;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ct.dao.UserDAO;
import com.ct.model.User;
import com.ct.service.UserService;

@RestController
public class UserResource {

	@Autowired
	UserService userService;

	@RequestMapping(value = "/login", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<User> authenticateUser() {
		User user = userService.getAuthenticatedUser();
		if (user != null)
			return new ResponseEntity<User>(user, HttpStatus.OK);
		else
			return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
	}

	@RequestMapping(value = "/test", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> test() {
		return new ResponseEntity<String>("Hello! Success!!", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/user", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<User> registerUser(@RequestBody UserDAO newUser) throws AuthenticationException {
		User userCreated=userService.createUser(newUser);
		System.out.println("Successfully registered user: " + newUser.getUserName());
		if (userCreated != null)
			return new ResponseEntity<User>(userCreated, HttpStatus.OK);
		else
			throw new AuthenticationException("Error creating new user");
		
	}
}
