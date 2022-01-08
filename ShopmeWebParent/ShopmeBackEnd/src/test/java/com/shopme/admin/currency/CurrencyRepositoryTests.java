package com.shopme.admin.currency;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Currency;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CurrencyRepositoryTests {

	@Autowired
	private CurrencyRepository repo;
	
	@Test
	public void testCreateFirstCurrency()
	{
		Currency usCurrency = new Currency( "Indian Rupee ", "₹", "INR");
		
		Currency current=repo.save(usCurrency);
		
		assertThat(current).isNotNull();
			
	}
	
	@Test 
	public void testFindAll() {	
		List<Currency> listCurrencies  = repo.findAllByOrderByNameAsc();
		
		/*
		 * listCurrencies.forEach(currency -> { System.out.println(currency); });
		 */
		
		listCurrencies.forEach(System.out::println);
		
	}
	
	
	
}
