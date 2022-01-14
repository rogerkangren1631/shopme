package com.shopme.common.entity;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CountryTest {
	Country country;
	
	@BeforeEach
	public void setUp() {
		
	country = new Country(300, "China", "CN");
	}
	
	@Test
	public void testCountryConstructor() {
		
		assertAll("Test Props Set",
				() ->assertEquals(300,country.getId(),  "ID Failed"),
				() ->assertEquals("China",country.getName(),  "Name Failed" ),
				() ->assertEquals( "CN",country.getCode(), "Code Failed" ) );
		
	}
	
	

}
