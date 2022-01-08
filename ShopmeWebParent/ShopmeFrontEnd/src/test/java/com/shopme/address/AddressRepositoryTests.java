package com.shopme.address;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class AddressRepositoryTests {

	@Autowired private TestEntityManager entityManager;
	@Autowired private AddressRepository repo;
	
	@Test 
	public void testAddNew() {
		Integer customerId = 37; 
		Integer countryId = 234;  //USA
		
		Address newAddress = new Address();
		newAddress.setCustomer( new Customer( customerId));
		newAddress.setCountry(  new Country(countryId) );
		newAddress.setFirstName("Yang");
		newAddress.setLastName( "Ren");
		newAddress.setPhoneNumber("205-123-4005");
		newAddress.setAddressLine1("1856 Russet Woods Ln");
		newAddress.setCity("Huntsville");
		newAddress.setState("Taiwan");
		newAddress.setPostalCode("10013");
		
		Address savedAddress = repo.save(newAddress);
		
		assertThat(savedAddress).isNotNull();	
	}
	
	@Test 
	public void testFindByCustomer() {
		Integer customerId = 37; 
		Customer customer = new Customer(customerId);
		
		List<Address> listAddresses = repo.findByCustomer(customer);
		assertThat(listAddresses).size().isEqualTo(3);
		
		listAddresses.forEach(System.out::println);	
	}
	
	@Test
	public void testFindByIdAndCustomer() {
		Integer customerId = 37;
		Integer addressId = 2;
		
		Address renAddress = repo.findByIdAndCustomer(addressId, customerId); 
		assertThat(renAddress).isNotNull();		
	}
	
	@Test
	public void testUpdate() {
		Integer customerId = 37;
		Integer addressId = 2;
		
		Address renAddress = repo.findByIdAndCustomer(addressId, customerId); 
		assertThat(renAddress).isNotNull();	
		renAddress.setDefaultForShipping(true);
		
		Address updatedAddress = repo.save(renAddress);
		
	}
	
	@Test
	public void testDeleteByIdAndCustomer() {
		Integer customerId = 37;
		Integer addressId = 1;
		
		 repo.deleteByIdAndCustomer(addressId, customerId);
		 
		Address renAddress = repo.findByIdAndCustomer(addressId, customerId); 
		assertThat(renAddress).isNull();		
	}
	
	@Test 
	public void testSetDefaultAddress() {
		Integer addressId = 6;
		repo.setDefaultAddress(addressId);
		
		Address addressDB = repo.findById(addressId).get();
		
		assertThat(addressDB.isDefaultForShipping()).isTrue();
			
	}
	
	 @Test
	 public void testSetNoNDefaultForOthers() {
		 Integer addressId = 3;
		 Integer customerId = 38;
		 repo.setDefaultAddress(addressId);
		 
		 repo.setNonDefaultForOthers(addressId, customerId);		 
	 }
	 
	 @Test 
	 public void testFindDefaultByCustomer() {
		 Integer customerId= 5;
		 Address address = repo.findDefaultByCustomer(customerId);
		 assertThat(address).isNotNull();
		 System.out.println(address);
		 
	 }
	 
	
}
