package com.shopme.admin.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.user.UserService;

@RestController
public class UserRestController {
	
	@Autowired
	private UserService service;
	
	@PostMapping("/users/check-email")
	public String checkDuplicateEmail(@RequestParam("id") Integer id,   @RequestParam("email") String email) {
		return service.isEmailUnique(id, email)? "OK" :"Duplicated"; 
	}
}
