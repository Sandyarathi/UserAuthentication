package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.ct.UserAuthenticationApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = UserAuthenticationApplication.class)
@WebAppConfiguration
public class UserAuthenticationApplicationTests {

	@Test
	public void contextLoads() {
	}

}
