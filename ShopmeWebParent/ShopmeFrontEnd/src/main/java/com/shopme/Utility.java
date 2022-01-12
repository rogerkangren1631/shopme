package com.shopme;

import java.security.Principal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import com.shopme.security.oauth.CustomerOAuth2User;
import com.shopme.setting.CurrencySettingBag;
import com.shopme.setting.EmailSettingBag;

public class Utility {
	public static String getSiteURL(HttpServletRequest request) {
		
		String siteURL = request.getRequestURI().toString();
		System.out.println("siteURL = " + siteURL );
		System.out.println("ServletPath = " + request.getServletPath() );
		
		String baseURL = "https://shopuswithren-frontend.herokuapp.com/" ; 
		return baseURL + siteURL.replace(request.getServletPath(), "");
	}
	
	public static JavaMailSenderImpl prepareMailSender( EmailSettingBag setting) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
			
		mailSender.setHost( setting.getHost());
		mailSender.setPort( setting.getPort() );
		mailSender.setUsername(setting.getUserName());
		mailSender.setPassword(setting.getPassword());
		
		
		Properties mailProperties = new Properties();
		mailProperties.setProperty("mail.smtp.auth", setting.getSmtpAuth());
		mailProperties.setProperty("mail.smtp.starttls.enable", setting.getSmtpSecured());

		mailSender.setJavaMailProperties(mailProperties);
		return mailSender;
		
	}
	
    public static String getEmailOfAuthenticatedCustomer(HttpServletRequest request) {
    	String customerEmail = null;
    	Principal principal =  request.getUserPrincipal();
    	if(principal ==null) return null;
    	
    	if(  principal instanceof RememberMeAuthenticationToken 
    			|| principal instanceof UsernamePasswordAuthenticationToken ) { // user login using Database password
    		customerEmail = request.getUserPrincipal().getName();
    		
    	}else if ( principal instanceof OAuth2AuthenticationToken) {  //user using Google or Facebook 
    		OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken)principal ;
    		CustomerOAuth2User aOAuth2User = (CustomerOAuth2User) oauth2Token.getPrincipal(); 
    		 customerEmail = aOAuth2User.getEmail();
    	}
    	 		
       return customerEmail;	
    }
    
    public static String formatCurrency(float amount, CurrencySettingBag settings) {
    	String symbol = settings.getSymbol();
    	String symbolPosition = settings.getSymbolPosition();
    	int decimalDigits = settings.getDecimalDigits();  	
    	String decimalPointType = settings.getDecimalPointType();
    	String thousandPointType = settings.getThousandPointType();
    	
    	String pattern = symbolPosition.equals("Before price") ?symbol : "";
    	pattern += "###,###";
    	
    	if( decimalDigits >0 ) {
    		pattern += ".";
    		
    		for(int count = 1; count<= decimalDigits ; count++) {
    			pattern += "#";
    		} 		
    	}
    	
    	pattern += symbolPosition.equals("After price") ?symbol : "";
    	
    	char thouandSeparator = thousandPointType.equals("POINT") ?'.' :',';
    	char decimalSeparator = decimalPointType.equals("POINT") ?'.' :',';
    	
    	DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
    	decimalFormatSymbols.setDecimalSeparator(decimalSeparator);
    	decimalFormatSymbols.setGroupingSeparator(thouandSeparator);
    	
    	DecimalFormat formatter = new DecimalFormat(pattern, decimalFormatSymbols);
    	
    	return formatter.format(amount);
    }
    
 
}
 