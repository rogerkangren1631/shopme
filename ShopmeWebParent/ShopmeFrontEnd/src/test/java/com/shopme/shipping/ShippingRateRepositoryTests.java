package com.shopme.shipping;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;



@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ShippingRateRepositoryTests {

	@Autowired private ShippingRateRepository repo;
	@Autowired private TestEntityManager entityManager;
	
	@Test 
	public void testFindByCountryAndState() {
		Integer countryId = 234; //USA
		Country country = new Country( countryId);
		String state = "New York";
	//	Country country = entityManager.find(Country.class, countryId );	
	//	String state = "Ohio";
        ShippingRate rate = repo.findByCountryAndState(country, state);
        
        assertThat( rate).isNotNull();
        System.out.println(rate);
        
		
		
	}
}
