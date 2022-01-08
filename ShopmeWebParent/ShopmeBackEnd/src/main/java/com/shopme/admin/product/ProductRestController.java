package com.shopme.admin.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.ProductNotFoundException;

@RestController
public class ProductRestController {
	@Autowired
	private ProductService service;
	
	
	@PostMapping("/products/check-name")
	public String checkDuplicateName(@RequestParam("id") Integer id,  @RequestParam("name") String name) {
		return service.isNameUnique(id, name)? "OK" :"Duplicated"; 
	}
	
	@GetMapping("/products/get/{id}")
	public ProductDTO getProductInfo(@PathVariable("id") Integer id)
			throws ProductNotFoundException {
		
		Product product = service.get(id); 
		return new ProductDTO( product.getName(), product.getMainImagePath(), 
				product.getDiscountPrice(), product.getCost());
	}
	
}
