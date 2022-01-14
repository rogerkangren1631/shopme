package com.shopme.common.entity;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CountryTest {
	Country country;
	
	@BeforeEach
	public void setUp() {
		
	country = new Country(300, "P.R.China", "ChinaTaiwan");
	}
	
	@Test
	public void testCountryConstructor() {
		
		assertAll("Test Props Set",
				() ->assertEquals(country.getId(), 300, "ID Failed"),
				() ->assertEquals(country.getName(), "P.R.China", "Name Failed" ),
				() ->assertEquals(country.getCode(), "ChinaTaiwan", "Code Failed" ) );
		
	}
	
	

}
