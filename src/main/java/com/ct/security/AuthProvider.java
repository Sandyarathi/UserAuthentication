package com.ct.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.ct.dao.UserDAO;
import com.ct.model.User;
import com.ct.service.UserService;

@Service
public class AuthProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		System.out.println("in authprovider");
		String userName = authentication.getName();
		String password = authentication.getCredentials().toString();
		if (userName != null && password != null) {
			System.out.println("Am I getting error here?");
			UserDAO userDAO = getUserDAO(userName);
			if (!userDAO.getPassword().equals(password)) {
				throw new BadCredentialsException("Password did not match");
			}
		}
		Authentication auth = new UsernamePasswordAuthenticationToken(userName,
				password);
		return auth;
	}

	private UserDAO getUserDAO(String userName) {
		// get UserDAO from Mongo
		UserDAO userDAO = UserService.userList.get(userName);
		if(userDAO!=null)
			return userDAO;
		else{
			System.out.println("Could not retrieve from list");
			return null;
		}
		
		
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);

	}

}
