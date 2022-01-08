package com.shopme.address;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.Utility;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.customer.CustomerService;

@Controller
public class AddressController {

	@Autowired
	private AddressService addressService;
	@Autowired
	private CustomerService customerService;

	@GetMapping("/address_book")

	public String showAddressBook(Model model, HttpServletRequest request) {
		Customer customer = getAuthenticatedCustomer(request);	
		List<Address> listAddresses = addressService.listAddressBook(customer);
		
		boolean usePrimaryAddressAsDefault = true;
	    for( Address address : listAddresses ) {
	    	if(address.isDefaultForShipping()) {
	    		usePrimaryAddressAsDefault = false;
	    		break;
	    	}    	
	    }
			
		model.addAttribute("listAddresses", listAddresses);
		model.addAttribute("customer", customer);
        model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);
		return "address_book/addresses";
	}

	
	@GetMapping("/address_book/new")
	public  String newAddress(Model model) {
		List<Country> listCountries = customerService.listAllCountries();

		model.addAttribute("listCountries", listCountries);		
		model.addAttribute("address", new Address());
		model.addAttribute("pageTitle", "Add New Address");
		
	    return "address_book/address_form";	
	}
	
	@GetMapping("/address_book/edit/{id}")
	public  String EditAddress(@PathVariable ("id") Integer id,  HttpServletRequest request, Model model) {
		List<Country> listCountries = customerService.listAllCountries();
		Customer customer = getAuthenticatedCustomer(request);
		Address address = addressService.get(id, customer.getId());
		model.addAttribute("listCountries", listCountries);		
		model.addAttribute("address", address);
		model.addAttribute("pageTitle", "Edit Address");
		
	    return "address_book/address_form";	
	}
	
	@PostMapping("/address_book/save")
	public String saveAddress(Address address, HttpServletRequest request, RedirectAttributes ra) {
		Customer customer = getAuthenticatedCustomer(request);	
		address.setCustomer(customer);
		addressService.save(address);
		
		String redirectURL = "redirect:/address_book";		
		String redirectOption = request.getParameter("redirect");
        if("checkout".equals(redirectOption)) {
    		redirectURL += "?redirect=checkout";
    	}
			
		ra.addFlashAttribute("message", "The address has been saved successfully.");
		
		return  redirectURL;
	}
	
	@GetMapping("/address_book/delete/{id}")
	public String deleteAddress(@PathVariable ("id") Integer id, HttpServletRequest request, RedirectAttributes ra) {
		Customer customer = getAuthenticatedCustomer(request);	
		
		Integer addressId = id;
		Integer customerId = customer.getId();
		addressService.delete(addressId,customerId );
		
		ra.addFlashAttribute("message", "Your selected address has been delete.");
		
		return "redirect:/address_book";
	}
	
	@GetMapping("/address_book/default/{id}")
	public String setAddressDefault(@PathVariable ("id") Integer addressId, HttpServletRequest request) {
		Customer customer = getAuthenticatedCustomer(request);	
		addressService.setDefaultAddress(addressId, customer.getId());
		String redirectURL = "redirect:/address_book";
		
		String redirectOption = request.getParameter("redirect");
    	if("cart".equals(redirectOption)) {
    		redirectURL = "redirect:/cart";
    	} else if("checkout".equals(redirectOption)) {
    		redirectURL = "redirect:/checkout";
    	}
		
		return redirectURL;
	}
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String customerEmail = Utility.getEmailOfAuthenticatedCustomer(request);
		return customerService.getCustomerByEmail(customerEmail);
	}
}
