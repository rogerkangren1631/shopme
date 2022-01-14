package com.shopme.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
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

	@Test
	public void testViewLoginPage() {
		assertEquals("login", controller.viewLoginPage(), "Return view is wrong.");	
	}
}

