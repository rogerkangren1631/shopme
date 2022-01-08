package com.shopme.admin.shippingrate;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Commit;

import com.shopme.admin.user.UserRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;

@Commit
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ShippingRateRepositoryTests {
  
	@Autowired
	private ShippingRateRepository repo;
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateFirstRate() {
		Country country = entityManager.find(Country.class, 14);
		
		ShippingRate shipRate = new ShippingRate();
		
		shipRate.setCountry(country);
		shipRate.setState("Queensland");
		shipRate.setDays(6);
		shipRate.setRate(8.15F);
		
		ShippingRate savedShipRate = repo.save(shipRate); 
		assertThat(savedShipRate).isNotNull();
		
	     System.out.println(savedShipRate);
	
	}
	
	@Test 
	public void testFindByCountryAndState() {
		Integer countryId = 14;
		String state = "Queensland";
		
		ShippingRate shipRate = repo.findByCountryAndState(countryId, state);
		
		assertThat(shipRate).isNotNull();
		System.out.println(shipRate);
		
	}
	
	@Test
	public void testUpdateCODSupport() {
		Integer id = 5;
		boolean codSupport = true;
		
		repo.updateCODSupport(id, codSupport);
		
		ShippingRate shipRate = repo.findById(id).get();
		
		assertThat( shipRate.isCodSupported()).isTrue(); 
		
		
	}
	
}
