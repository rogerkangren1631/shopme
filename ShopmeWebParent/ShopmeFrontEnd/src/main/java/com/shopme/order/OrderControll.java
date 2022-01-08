package com.shopme.order;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.Utility;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.order.Order;
import com.shopme.customer.CustomerService;

@Controller
public class OrderControll {
	@Autowired private OrderService orderService;
	@Autowired private CustomerService customerService;
	
	@GetMapping("/orders")
	public String listFirstPage(Model model, HttpServletRequest request ) {
		
		return listOrderByPage(model, request, 1, "orderTime", "desc", null); 
		
	}

	@GetMapping("/orders/page/{pageNum}")
	public String listOrderByPage(Model model, HttpServletRequest request,
			@PathVariable(name = "pageNum") int pageNum, 
			String sortField, String sortDir,String orderKeyword) {
		
		Customer customer = getAuthenticatedCustomer( request);
		
		Page<Order> page = orderService.listForCustomerByPage(customer, pageNum, sortField, sortDir, orderKeyword);
		List<Order> listOrders = page.getContent();
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listOrders", listOrders);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);	
		model.addAttribute("orderKeyword", orderKeyword);	
		model.addAttribute("moduleURL", "/orders");
		
		long startCount = (pageNum-1)*orderService.ORDERS_PER_PAGE +1 ;
		model.addAttribute("startCount", startCount);
		long endCount = startCount + orderService.ORDERS_PER_PAGE -1; 
		
		if( endCount >  page.getTotalElements() ){
			endCount = page.getTotalElements();
		}
		model.addAttribute("endCount", endCount);

		return "orders/orders_customer";
	}
	
	@GetMapping("/orders/detail/{id}")
	public String viewOrderDetails(@PathVariable("id") Integer id, Model model,
			RedirectAttributes ra,
			HttpServletRequest request) {
		Customer customer = getAuthenticatedCustomer( request);
		
		Order order = orderService.getOrder(id, customer);
		model.addAttribute("order", order);
		
		return "orders/order_details_modal";
	}
		
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String customerEmail = Utility.getEmailOfAuthenticatedCustomer(request); 	
		return customerService.getCustomerByEmail(customerEmail);		
	}
}
