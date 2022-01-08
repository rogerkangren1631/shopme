package com.shopme.customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Commit;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;




@Commit
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CustomerRepositoryTest {
	@Autowired
	private CustomerRepository repo;

	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateCustomer1() {
		Integer countryId = 234;  //USA
		Country country = entityManager.find(Country.class, countryId) ;
		
		Customer customer = new Customer();
		customer.setCountry(country);
		customer.setFirstName("David");
		customer.setLastName("Fountaine");
		customer.setPassword("password123");
		customer.setEmail("davidfountaine@gmail.com");
		customer.setPhoneNumber("205-462-7518");
		customer.setAddressLine1("1927 West Driver");
		customer.setCity("Sacramento");
		customer.setState("California");
		customer.setPostalCode("95876");
		customer.setCreatedTime(new Date());
		
		Customer savedCustomer = repo.save(customer);
		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);	
	}
	
	@Test
	public void testCreateCustomer2() {
		Integer countryId = 106;  //India
		Country country = entityManager.find(Country.class, countryId) ;
		
		Customer customer = new Customer();
		customer.setCountry(country);
		customer.setFirstName("Sanya");
		customer.setLastName("Lad");
		customer.setPassword("password456");
		customer.setEmail("sanya.lad2020@gmail.com");
		customer.setPhoneNumber("02224928052");
		customer.setAddressLine1("173 , A-, Shah @@Nahar Indl.estate, Sunmill Road");
		customer.setCity("Mumbai");
		customer.setState("Maharashtra");
		customer.setPostalCode("400013");
		customer.setCreatedTime(new Date());
		
		Customer savedCustomer = repo.save(customer);
		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);	
	}
	
	@Test 
	public void testListCustomers() {
		
		Iterable<Customer> customers = repo.findAll(); 
		customers.forEach(System.out::println);
		
		assertThat(customers).hasSizeGreaterThan(0); 
	}
	
	@Test
	public void testUpdateCustomer() {
		
		Integer customerId = 1;
		String lastName = "Stanford";
		
		Customer customer = repo.findById(customerId).get(); 
		customer.setLastName(lastName);
		customer.setEnabled(true);
		
		Customer updatedCustomer = repo.save(customer);		
		assertThat(updatedCustomer.getLastName()).isEqualTo(lastName);		
	}
	
	@Test 
	public void testFindByEmail() {
		String email = "davidfountaine@gmail.com"; 
		Customer customer = repo.findByEmail(email);
		
		assertThat(customer).isNotNull();		
	}
	@Test
	public void testDeleteCustomer() {
		Integer id = 2;
		repo.deleteById(id);
		
		Optional<Customer> customer = repo.findById(id);
		assertThat(customer).isNotPresent();
		
	}
	
	@Test
	public void testUpdateAuthenticationType() {
		Integer id = 37;
		AuthenticationType type = AuthenticationType.FACEBOOK;
		repo.updateAuthenticationType(id, type);
		
		Customer customer = repo.findById(id).get();
		assertThat(customer.getAuthenticationType()).isEqualTo(AuthenticationType.FACEBOOK);
		
	}
	
	
}
