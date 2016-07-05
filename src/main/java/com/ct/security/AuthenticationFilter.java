package com.ct.security;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.access.channel.ChannelProcessingFilter;

import com.ct.exceptions.AuthenticationException;
import com.ct.service.AuthHelper;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthenticationFilter extends ChannelProcessingFilter {

	private final AuthHelper authHelper;

	public AuthenticationFilter(AuthHelper authHelper) {
		this.authHelper = authHelper;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest) req;

		if (WebSecurityConfig.AUTHENTICATION_PATH.equals(request
				.getServletPath())) {
			chain.doFilter(req, res);
			return;
		}

		String authToken = extractToken(request);

		if (checkToken(authToken)) {
			chain.doFilter(req, res);
		} else {
			writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
					"Invalid or missing authorization token");
		}
	}

	private void writeErrorResponse(HttpServletResponse response, int status,
			String message) throws JsonGenerationException,
			JsonMappingException, IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("timestamp", LocalDateTime.now());
		map.put("status", status);
		map.put("error", "Unauthorized");
		map.put("exception", AuthenticationException.class.getName());
		map.put("message", message);
		mapper.writeValue(response.getWriter(), map);
	}

	private boolean checkToken(String authToken) {
		return authHelper.checkToken(authToken);
	}

	private String extractToken(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");
		if (authorization != null && authorization.matches("^Token \\w+$")) {
			return authorization.split(" ", 2)[1];
		}
		return request.getParameter("auth");
	}
}