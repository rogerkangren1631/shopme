package com.shopme;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

public class MainControllerTest {
	MainController controller;
	Model model;
	
@BeforeEach
void setUp() {
	controller = new MainController();	
	model = null;
	
}

 @Test
 public void testViewHomePage() {
	  
	 assertEquals("index", controller.viewHomePage(model));
 }
 
}
