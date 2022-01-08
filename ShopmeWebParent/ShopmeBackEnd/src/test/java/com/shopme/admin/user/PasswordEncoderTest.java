package com.shopme.admin.user;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {
	@Test
	public void testEncodePassword() {
		
		  BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); 
		  String  rawPassword = "roger2021";
		  
		  String encodedPassword = passwordEncoder.encode(rawPassword);
		  
		  System.out.println(encodedPassword); boolean isTrue =
		  passwordEncoder.matches(rawPassword, encodedPassword); assertTrue(isTrue);
		  assertThat(isTrue).isTrue();
		 
		
				
		
		
	}
}
