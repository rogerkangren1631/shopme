package com.shopme.customer;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shopme.Utility;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.setting.EmailSettingBag;
import com.shopme.setting.SettingService;

@Controller
public class ForgotPasswordController {
	@Autowired private CustomerService  customerService;
	@Autowired private SettingService settingService;
	
	@GetMapping("/forgot_password")
	public String showRequestForm() {
		
		return "customer/forgot_password_form";
	}
	
	@PostMapping("/forgot_password")
	public String processRequestForm(HttpServletRequest request, Model model) {
		String email = request.getParameter("email");
		
		try {
			String token = customerService.updateResetPasswordToken( email );
			String link = Utility.getSiteURL(request) + "reset_password?token=" + token;
			sendEmail(link, email);
			System.out.println("The link is  : '" + link + "' for the email " + email);
			
			model.addAttribute("message", "We have sent a reset password link to your email, "
					+ " Please check it."); 
		} catch (CustomerNotFoundException e) {

			model.addAttribute("error", e.getMessage());
		} catch (UnsupportedEncodingException | MessagingException e) {
			model.addAttribute("errot", "Could not send email."); 
		}
		
		return "customer/forgot_password_form";
	}
	
	private void sendEmail(String link, String email ) throws UnsupportedEncodingException, MessagingException {
		
	EmailSettingBag emailSettings = settingService.getEmailSettings();
		
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
		
		String toAddress = email;
		String subject = "Here's the link  to reset your password " ;
		
		String content = "<p> Hello, </p>" 
				+ "<p> You have requested to reset your password. </p>" 
				+ "<p> Click the link below to change your password: </p> "
				+ "<p> <a href=\"" + link + "\" >Change my password. </a></p> "
				+ "<br> "
				+ " <p> Ignore this email if you do remember your password, "
				+ "or you have not made the request.  </p> "; 
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		helper.setText(content, true);
		mailSender.send(message);		
	}
	
	@GetMapping("/reset_password")
	public String showResetPasswordForm(@RequestParam ("token") String token, Model model) {	
		Customer customer = customerService.getByResetPasswordToken(token);
		if( customer !=null) {
			model.addAttribute("token", token);
					
		}else {
			model.addAttribute("message", "Invalid Token");
			model.addAttribute("pageTitle", "Invalid Token");
			return "message";
		}
		return "customer/reset_password_form";
	}
	
	@PostMapping("/reset_password")
	public String processResetForm(HttpServletRequest request, Model model ){
		String token = request.getParameter("token");
		String newPassword = request.getParameter("password");
		try {
			customerService.updatePassword(token, newPassword);
			model.addAttribute("message", "You have successfully changed your password.");
			model.addAttribute("title", " Reset Your Password " );
			model.addAttribute("pageTitle", "Reset password.");
			return "message";
			
		} catch (CustomerNotFoundException e) {
			model.addAttribute("message", "Invalid Token");
			model.addAttribute("pageTitle", "Invalid Token");
			return "message";
		}
		
	
	}
	
}


