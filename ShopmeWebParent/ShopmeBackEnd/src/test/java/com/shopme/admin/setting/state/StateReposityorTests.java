package com.shopme.admin.setting.state;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Commit;

import com.shopme.admin.product.ProductRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingCategory;

@Commit
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class StateReposityorTests {
	@Autowired
	private StateRepository repo;
	
	@Autowired
	private  TestEntityManager entityManager;
	

	@Test
	public void testCreateState() {
		Integer countryId = 2;
		Country country = entityManager.find(Country.class, countryId);
	//	State state = repo.save(  new State("Alabama", country));
	//	State state = repo.save(  new State("Alaska", country)); 
	//	State state = repo.save(  new State("Arizona", country));
	//	State state = repo.save(  new State("California", country));
		//State state = repo.save(  new State("Florida", country));
		
		//State state = repo.save(  new State("Beijing", country));
	//	State state = repo.save(  new State("Xing Jiang", country));
	//	State state = repo.save(  new State("Hu Bei", country));
		//State state = repo.save(  new State("He Nan", country));	
		
		
		State state = repo.save(  new State("Jiang Su", country));
		assertThat(country).isNotNull();
		assertThat(country.getId()).isGreaterThan(0);
		assertThat(state).isNotNull();
		assertThat(state.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void testFindByCountryOrderByNameAsc() {
		Integer countryId = 2;
		Country country = entityManager.find(Country.class, countryId);
		
		List<State> listStates= repo.findByCountryOrderByNameAsc(country);
		listStates.forEach(System.out::println);
		
		assertThat(listStates.size()).isGreaterThan(0); 
				
				
		
	}
	
}
