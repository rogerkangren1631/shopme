package com.shopme.shoppingcart;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.Utility;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.customer.CustomerService;

@RestController
public class ShoppingCartRestController {

	@Autowired private ShoppingCartService cartService;
	@Autowired private CustomerService customerService;
	
	@PostMapping("/cart/add/{productId}/{quantity}")
	public String addProductToCart(@PathVariable("productId") Integer productId, 
			@PathVariable("quantity") Integer quantity, HttpServletRequest request) {
		
		try {
			Customer customer = getAuthenticatedCustomer(request);
			Integer updatedQuantity = cartService.addProduct(productId, quantity, customer);
					
			return updatedQuantity + " item(s) of this product were added to your shopping cart.";
		} catch (CustomerNotFoundException e) {

			return "You must login to add this product to cart.";
		} catch(ShopingCartException ex) {		
			return ex.getMessage();
		}

	}
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request)
			throws CustomerNotFoundException {

		String customerEmail = Utility.getEmailOfAuthenticatedCustomer(request); 
		if(customerEmail == null) {
			throw new CustomerNotFoundException("No authenticated customer"); 
		}
				
		return customerService.getCustomerByEmail(customerEmail);		
	}
	
	@PostMapping("/cart/update/{productId}/{quantity}")
	public String updateQuantity(@PathVariable("productId") Integer productId, 
			@PathVariable("quantity") Integer quantity, HttpServletRequest request) {				
		try {
			Customer customer = getAuthenticatedCustomer(request);
			float suntotal = cartService.upadteQuantity(productId, quantity, customer);
					
			return String.valueOf(suntotal) ;
		} catch (CustomerNotFoundException e) {

			return "You must login to change quantity of product.";
		}
	}
	
	
	@DeleteMapping("/cart/remove/{productId}")
	public String removeProductFromCart(@PathVariable("productId") Integer productId, 
			HttpServletRequest request) {
		try {
			Customer customer = getAuthenticatedCustomer(request);
			cartService.removeProduct(productId, customer);
			return "The product has been remove from your shopping cart. "; 
		}  catch (CustomerNotFoundException e) {
			return "You must login to remove product.";
		}
	}
	
}
