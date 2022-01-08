package com.shopme.admin.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;


import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;

import com.shopme.common.exception.CustomerNotFoundException;


@Controller
public class CustomerController {

	@Autowired private CustomerService service;

	@GetMapping("/customers")
	public String listFirstPage(Model model) {
		return "redirect:/customers/page/1?sortField=firstName&sortDir=asc";
	}

	@GetMapping("/customers/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName="listCustomers", moduleURL="/customers") PagingAndSortingHelper helper, 
			@PathVariable(name = "pageNum") int pageNum, Model model) {
		
		service.listByPage(pageNum, helper);
		
		return "customers/customers";
	}
	
	@GetMapping("/customers/{id}/enabled/{status}")
	public String updatedCustomerEnabledStatus(@PathVariable(name = "id") Integer id,
			@PathVariable(name = "status") boolean enabled, RedirectAttributes redirectAttributes)
			throws CustomerNotFoundException {
		service.updateCustomerEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The customer ID: " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/customers";
	}
	
	@GetMapping("/customers/detail/{id}")
	public String viewCustomerDetails(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Customer customer = service.get(id);
			model.addAttribute("customer", customer);
			return "customers/customer_detail_modal";

		} catch (CustomerNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", "Cound not find customer id " + id);
		}
		return "redirect:/customers";
	}
	
	@GetMapping("/customers/edit/{id}")
	public String editCustomer(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {

		try {
			Customer customer = service.get(id);
			
			List<Country> countries = service.listAllCountries();
			model.addAttribute("listCountries", countries);
			model.addAttribute("customer", customer);
			model.addAttribute("pageTitle", "Edit Customer (ID: " + id + ")");

			return "customers/customer_form";
		} catch (CustomerNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", "Cound not find customer id " + id);
		}
		return "redirect:/customers";
	}
	
	@PostMapping("/customers/save")
	public String saveCustomer(Customer customer, Model model, RedirectAttributes ra) {	
		service.save(customer);
		ra.addFlashAttribute("message", "The customer ID "+ customer.getId() + " has been updated." );
		return "redirect:/customers";
	}
	
	
	
	@GetMapping("/customers/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id,
			RedirectAttributes redirectAttributes) throws CustomerNotFoundException {

		try {

			service.delete(id);
			redirectAttributes.addFlashAttribute("message", "The customer ID: " + id + " has been deleted successfully");

		} catch (CustomerNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", "Cound not find customer id " + id);

		}
		return "redirect:/customers";
	}
}
