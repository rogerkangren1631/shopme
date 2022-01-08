package com.shopme.admin.setting.country;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;

import com.shopme.admin.product.ProductRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingCategory;

@Commit
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CountryReposityorTests {
	@Autowired
	private CountryRepository repo;
	

	@Test
	public void testCreateCountry() {
		//Country country = repo.save( new Country("United American States", "US")); 
		//Country country = repo.save( new Country("China", "CN")); 
		Country country = repo.save( new Country("Japan", "JP"));
		assertThat(country).isNotNull();
		assertThat(country.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void testFindAllByOrderByNameAsc() {
		List<Country> listCountries = repo.findAllByOrderByNameAsc();
		listCountries.forEach(System.out::println);
		
		assertThat(listCountries.size()).isGreaterThan(0); 
				
				
		
	}
	
}
