package com.shopme.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MainControllerTest {
	MainController controller ;
	
	@BeforeEach
	void setUp() {
		controller = new MainController();	
	}
	
	@Test 
	public void testViewHomePage() {
		assertEquals("index", controller.viewHomePage(), "Return view is wrong.");		
	}

	@DisplayName("Test ViewLoginPage for user without Authentication ")
	@Test
	public void testViewLoginPage() {
		assertEquals("login", controller.viewLoginPage(), "Return view is wrong.");	
	}
	
	@Disabled(value="Disabled until we learn how to do it- Note:2022-1-14")
	@Test
	public void testViewLoginPageWithAuthentication() {
		//Disable this test using @Disable since I can not put a authenticated user 
		assertEquals("redirect:/", controller.viewLoginPage(), "Return view is wrong.");	
	}
}

