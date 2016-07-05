package com.ct.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@Service
public class AuthHelper {

	@Autowired
	private UserService userService;

	public static final int TOKEN_EXPIRATION_PERIOD_IN_MINUTES = 12 * 60;

	private final Cache<String, Authentication> tokenStore;

	@Autowired
	public AuthHelper(@Value("${user.cacheSize:1000}") int userCacheSize) {
		tokenStore = CacheBuilder
				.newBuilder()
				.maximumSize(10000)
				.expireAfterWrite(TOKEN_EXPIRATION_PERIOD_IN_MINUTES,
						TimeUnit.MINUTES).build();
	}

	public Authentication getAuthentication() {
		final SecurityContext securityContext = SecurityContextHolder
				.getContext();
		return securityContext.getAuthentication();
	}

	public String getUsername() {
		final Authentication authentication = getAuthentication();
		return authentication != null ? authentication.getName() : null;
	}

	public void saveToken(String token) {
		tokenStore.put(token, getAuthentication());
	}

	public boolean checkToken(String authToken) {
		Authentication authentication = authToken != null ? tokenStore
				.getIfPresent(authToken) : null;
		final SecurityContext securityContext = SecurityContextHolder
				.getContext();
		securityContext.setAuthentication(authentication);
		return authentication != null;
	}

}
