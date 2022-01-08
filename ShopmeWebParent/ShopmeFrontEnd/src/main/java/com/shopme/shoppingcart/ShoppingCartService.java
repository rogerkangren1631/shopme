package com.shopme.shoppingcart;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.product.Product;
import com.shopme.product.ProductRepository;

@Service
@Transactional
public class ShoppingCartService {

	@Autowired  private CartItemRepository cartRepo;
	@Autowired  private ProductRepository productRepo;
	

	
	public Integer addProduct(Integer productId, Integer quantity, Customer customer) throws ShopingCartException {
		Integer updateQuantity = quantity;
		Product product = new Product (productId);
	
		CartItem cartItem = cartRepo.findByCustomerAndProduct(customer, product);
		
		if(cartItem != null) {
			updateQuantity = cartItem.getQuantity() + quantity;
			if( updateQuantity > 5) {
				
				throw new ShopingCartException("Cound not add more " + quantity +" items" 
				   + " because there is already  " + cartItem.getQuantity() + " item(s) " 
				   + " in your shopping cart. Maximum allowed quantity is 5. ");
				
			}		
		}else {
			cartItem = new CartItem();
			cartItem.setCustomer(customer);
			cartItem.setProduct(product);		
		}
		cartItem.setQuantity(updateQuantity);	
		cartRepo.save(cartItem);
		
		return updateQuantity;	
	}
	
	public List<CartItem> listCartItems(Customer customer) {
		
		return cartRepo.findByCustomer(customer);		
	}
	
	public float upadteQuantity(Integer productId, Integer quantity, Customer customer ) {
		cartRepo.updateQuantity(quantity, customer.getId(), productId);
		Product product = productRepo.findById(productId).get(); 
		float subtotal = product.getDiscountPrice()* quantity;		
		return subtotal;
	}
	
   public void removeProduct(Integer productId, Customer customer) { 
	   cartRepo.deleteByCustomerAndProduct(customer.getId(), productId);
   }
	
	public void deleteByCustomer(Customer customer) {
		cartRepo.deleteByCustomer(customer.getId());
	}
	
	
}
