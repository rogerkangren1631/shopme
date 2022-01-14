package com.shopme.customer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.Utility;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.security.CustomerUserDetails;
import com.shopme.security.oauth.CustomerOAuth2User;
import com.shopme.setting.EmailSettingBag;
import com.shopme.setting.SettingService;

@Controller
public class CustomerController {
	
	@Autowired private CustomerService customerService;
	@Autowired private SettingService settingService;
	
	
	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		List<Country> listCountries = customerService.listAllCountries();
		
		model.addAttribute("listCountries", listCountries);
		model.addAttribute("pageTitle", "Customer Registration");
		model.addAttribute("listCountries", listCountries);
		model.addAttribute("customer", new Customer());
		return "register/register_form";	
	}
	
	@PostMapping("/create_customer")
	public String CreateCustomer(Customer customer, Model model, 
			HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {		
		customerService.registerCustomer(customer);
		sendVerificationEmail(request, customer);
		model.addAttribute("pageTitle", "Registration Succeeded");
		
		return "register/register_success";
	}

	private void sendVerificationEmail(HttpServletRequest request, Customer customer) throws UnsupportedEncodingException, MessagingException  {
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
		
		String toAddress = customer.getEmail();
		String subject = emailSettings.getCustomerVerifySubject();
		String content = emailSettings.getCustomerVerifyContent();
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		content = content.replace("[[name]]", customer.getFullName() ); 
		String verifyURL = Utility.getSiteURL(request)+ "verify?code="+customer.getVerificationCode(); 
		content = content.replace("[[URL]]", verifyURL); 

		helper.setText(content, true);
		mailSender.send(message);
		System.out.println("toAddress: " + toAddress );
		System.out.println("Verify URL: " + verifyURL );
	}

	@GetMapping("/verify")
	public String verifyAccount(@RequestParam("code") String code, Model model ) {
		
		boolean verified =customerService.verify(code);

		return "register/" + (verified ? "verify_success" : "verify_fail");
    }
	
    @GetMapping("/account_details")
    public String viewAccountDetails(Model model, HttpServletRequest request) {
        String email = Utility.getEmailOfAuthenticatedCustomer(request);
        Customer customer = customerService.getCustomerByEmail(email) ; 
        List<Country> listCountries = customerService.listAllCountries();
        
        model.addAttribute("customer", customer);
        model.addAttribute("listCountries", listCountries);
    
    	return "customer/account_form";
	}
       
    @PostMapping("/update_account_details")
    public String updateAccountDetails(Model model, Customer customer,  RedirectAttributes ra,
    		HttpServletRequest request) {
    	customerService.update(customer); 
    	updateNameForAuthenticatedCustomer(customer, request);
     	ra.addFlashAttribute("message", "Your account details have been updated.");
     	
    	String redirectOption = request.getParameter("redirect");
    	String redirectURL = "redirect:/account_details";
    	
    	if("address_book".equals(redirectOption)) {
    		redirectURL = "redirect:/address_book";
    	}else if("cart".equals(redirectOption)) {
    		redirectURL = "redirect:/cart";
    	}else if("checkout".equals(redirectOption)) {
    		redirectURL = "redirect:/address_book?redirect=checkout";
    	}	
   
    	return redirectURL;
    }

	private void updateNameForAuthenticatedCustomer(Customer customer, HttpServletRequest request) {
    	Principal principal =  request.getUserPrincipal();
    	
    	if(  principal instanceof RememberMeAuthenticationToken 
    			|| principal instanceof UsernamePasswordAuthenticationToken ) { // user login using Database password
    			CustomerUserDetails userDetails = getCustomerUserDetailsObject(principal );
    			
    			Customer authenticatedCustomer = userDetails.getCustomer();
    			authenticatedCustomer.setFirstName(  customer.getFirstName());
    			authenticatedCustomer.setLastName( customer.getLastName());
    		
    	}else if ( principal instanceof OAuth2AuthenticationToken) {  //user using Google or Facebook 
    		OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken)principal ;
    		CustomerOAuth2User aOAuth2User = (CustomerOAuth2User) oauth2Token.getPrincipal(); 
    		String fullName = customer.getFullName();
    		aOAuth2User.setFullName(  fullName);
    	}	
	}

	private CustomerUserDetails getCustomerUserDetailsObject(Principal principal) {
		CustomerUserDetails userDetails = null;
		if(  principal instanceof UsernamePasswordAuthenticationToken ) {
			UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
			userDetails = (CustomerUserDetails) token.getPrincipal(); 
		} else if(  principal instanceof RememberMeAuthenticationToken ) {
			RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) principal;
			userDetails = (CustomerUserDetails) token.getPrincipal(); 
		}
		
		return userDetails;
	}
	
}
