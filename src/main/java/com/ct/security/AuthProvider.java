package com.ct.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.ct.dao.UserDAO;

@Service
public class AuthProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		String userName = authentication.getName();
		String password = authentication.getCredentials().toString();
		if (userName != null && password != null) {
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
		UserDAO userDAO = new UserDAO();

		// test code, delete after mongo implementation
		if (userName.equals("sandhya")) {
			userDAO.setFirstName("Sandhya");
			userDAO.setLastName("Das");
			userDAO.setUserName("sandhya");
			userDAO.setPassword("sandy");
			return userDAO;
		}
		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);

	}

}
