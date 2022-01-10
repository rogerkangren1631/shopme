package com.shopme.admin.shippingrate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;

@Controller
public class ShippingRateController {
	@Autowired private ShippingRateService service;
	
	private String defaultRedirectedURL = "redirect:/shipping_rates/page/1?sortField=country&sortDir=asc";
	
	@GetMapping("/shipping_rates")
	public String listFirstPage() {
		return defaultRedirectedURL;
	}
	
	@GetMapping("/shipping_rates/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName="shippingRates", moduleURL="/shipping_rates") PagingAndSortingHelper helper, 
			@PathVariable(name = "pageNum") int pageNum) {
	
		service.listByPage(pageNum, helper);
	 return "shipping_rates/shipping_rates";
	}
	
	@GetMapping("/shipping_rates/new")
	public String newRate(Model model) {
		List<Country> listCountries =service.listAllCountries();
		
		model.addAttribute("listCountries", listCountries);
		model.addAttribute("rate",new ShippingRate());
		model.addAttribute("pageTitle", "New Rate"); 
		
		return "shipping_rates/shipping_rate_form";		
	}
	
	@PostMapping("/shipping_rates/save")
	public String saveRate(ShippingRate rate , RedirectAttributes ra ) {
		try {
			service.save(rate);
			ra.addFlashAttribute("message", "The shipping rate has been saved successfully."); 
		} catch (ShippingRateAlreadyExistsException ex) {

			ra.addFlashAttribute("message", ex.getMessage()); 
		}
		
		return defaultRedirectedURL;
	}
	
	@GetMapping("/shipping_rates/edit/{id}")
	public String editRate (@PathVariable (name="id") Integer id,
			Model model,  RedirectAttributes ra ) {			
		try {
			ShippingRate rate = service.get(id);
			List<Country> listCountries =service.listAllCountries();
			
			model.addAttribute("listCountries", listCountries);
			model.addAttribute("rate",rate);
			model.addAttribute("pageTitle", "Edit Rate (" + id + ")"); 
			
			return "shipping_rates/shipping_rate_form";
						
		} catch (ShippingRateNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			
			return defaultRedirectedURL;
		}
	}
	
	@GetMapping ("/shipping_rates/delete/{id}")
	public String deleteRate(@PathVariable (name="id") Integer id,
			Model model,  RedirectAttributes ra ) {	
		try {
			service.delete(id);
			ra.addFlashAttribute("message", "The shipping rate ID "+ id + " has been deleted.");
		} catch (ShippingRateNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
		} 		
		return defaultRedirectedURL;
	}
	
	@GetMapping("/shipping_rates/cod/{id}/enabled/{supported}")
	public String updateCODSupport(@PathVariable(name="id") Integer id, 
			@PathVariable (name="supported") boolean supported, 
			Model model, RedirectAttributes ra ) {	
		try {
			String result = supported ? "enbled" :"disabled"; 	
			service.updateCODSupport(id, supported);
			ra.addFlashAttribute("message", "COD support for shipping rate ID "+id + " has been "+ result );
		} catch (ShippingRateNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage()); 
		} 
		return defaultRedirectedURL;
	}
	
}
