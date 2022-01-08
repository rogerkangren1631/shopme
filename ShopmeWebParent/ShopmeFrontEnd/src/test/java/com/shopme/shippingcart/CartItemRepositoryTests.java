package com.shopme.shippingcart;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.product.Product;
import com.shopme.shoppingcart.CartItemRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CartItemRepositoryTests {
	
	@Autowired private CartItemRepository repo;
	@Autowired private TestEntityManager entityManager;
	
	@Test
	public void testSaveItem() {
		Integer customerId = 37;
		Integer productId = 73;
		
		Customer customer = entityManager.find(Customer.class, customerId);
		Product product = entityManager.find(Product.class, productId);
		
		CartItem newItem = new CartItem();
		newItem.setCustomer(customer);
		newItem.setProduct(product);
		newItem.setQuantity(1);
		
		CartItem savedItem =  repo.save(newItem);
		
		assertThat(savedItem.getId()).isGreaterThan(0);	
	}
	
	@Test
	public void testSave2Item() {
		Integer customerId = 38;
		Integer productId = 73;
		
		Customer customer = entityManager.find(Customer.class, customerId);
		Product product = entityManager.find(Product.class, productId);
		
		CartItem newItem1 = new CartItem();
		newItem1.setCustomer(customer);
		newItem1.setProduct(product);
		newItem1.setQuantity(2);
		
		CartItem newItem2 = new CartItem();
		newItem2.setCustomer(new Customer(customerId));
		newItem2.setProduct(new Product(8));
		newItem2.setQuantity(3);
		
		Iterable<CartItem> savedItems =  repo.saveAll(List.of( newItem1, newItem2) );
		
		assertThat(savedItems).size().isGreaterThan(0)	;
	}
	
	@Test
	public void testFindByCustomer() {
		Integer customerId = 38;
		Customer customer = entityManager.find(Customer.class, customerId);
		
		List<CartItem> items =  repo.findByCustomer(customer);
		
		assertThat(items).size().isEqualTo(2);
		
		for( CartItem item : items ){
		   System.out.println(item);
		}
	}
	
	@Test
	public void testFindByCustomerAndProduct() {
		Integer customerId = 38;
		Integer productId = 73;
		
		Customer customer = entityManager.find(Customer.class, customerId);
		Product product = entityManager.find(Product.class, productId);
		
		CartItem cartItemInDB = repo.findByCustomerAndProduct(customer, product); 
		assertThat(cartItemInDB).isNotNull();
		System.out.println(cartItemInDB);
		
	}
	
	@Test
	public void testUpdateQuantity() {
		Integer customerId = 38;
		Integer productId = 73;
		
		repo.updateQuantity(5, customerId, productId);
		
		CartItem cartItemInDB = repo.findByCustomerAndProduct(new Customer( customerId), new Product( productId) ); 
		
		assertThat( cartItemInDB.getQuantity() ).isEqualTo(5); 
			
	}
	
	@Test 
	public void testDeleteByCustomerAndProduct() {
		Integer customerId = 38;
		Integer productId = 73;
		
		repo.deleteByCustomerAndProduct(customerId, productId);
		CartItem cartItemInDB = repo.findByCustomerAndProduct(new Customer( customerId), new Product( productId) ); 
		
		assertThat(cartItemInDB).isNull();
		
		
	}
}
