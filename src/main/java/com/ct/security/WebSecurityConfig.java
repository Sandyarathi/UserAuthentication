package com.ct.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import com.ct.service.AuthHelper;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	public final static String AUTHENTICATION_PATH = "/login";
	public final static String USER_REGISTRATION_PATH = "/user";

	@Autowired
	AuthProvider authProvider;

	@Autowired
	AuthHelper authHelper;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.addFilterBefore(new AuthenticationFilter(authHelper),AnonymousAuthenticationFilter.class)
				.authorizeRequests()
				.regexMatchers(AUTHENTICATION_PATH).fullyAuthenticated()
				.and().httpBasic()
				.and().csrf().disable()
				.sessionManagement().disable();

	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web.ignoring().antMatchers(USER_REGISTRATION_PATH);
	}
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.authenticationProvider(authProvider).eraseCredentials(false);
	}
}