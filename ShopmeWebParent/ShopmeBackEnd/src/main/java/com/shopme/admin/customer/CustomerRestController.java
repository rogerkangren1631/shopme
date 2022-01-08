package com.shopme.admin.customer;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {

	@Autowired private CustomerService service;
	
	@PostMapping("/customers/check_unique_email")
	public String checkDuplicatEmail(@RequestParam("id") Integer id, @RequestParam("email") String email) {
		
		return service.isEmailUnique(id, email) ?"OK":"Duplicated"; 		 
	}
}
