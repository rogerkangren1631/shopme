package com.shopme.checkout.paypal;



import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class PayPalTests {
	private static final String BASE_URL= "https://api.sandbox.paypal.com";
	private static final String GET_ORDER_API =  "/v2/checkout/orders/";
	private static final String CLIENT_ID = "AYCTNMTnSnkiYx81L1-zMxgtXFIMxFQJWbpj1EHQZXd23D83yBI2PHqCDbC2rpXmvyhSQs4CRyBVL-d-";
	private static final String CLIENT_SECRET = "EEcUaoOOhbdw3r2A6GAhj6FNJaj_ALFBHfQm419G7lrxm3y3FHtPXrftRsu6EoZkslvK01G-qN6OwYDN";

 @Test 
 public void testGetOrderDetails() {
	 String orderId= "8YP919299F888035F";
	 String requestURL = BASE_URL + GET_ORDER_API + orderId;
	 
	 HttpHeaders headers = new HttpHeaders();
	 headers.setAccept( Arrays.asList(MediaType.APPLICATION_JSON));
	 headers.add("Accept-Language", "en-US");
	 headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET );
	 
	 HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
	 RestTemplate restTemplate = new RestTemplate();
	 ResponseEntity<PayPalOrderResponse> response  = restTemplate.exchange(requestURL, HttpMethod.GET, request, PayPalOrderResponse.class);
	 
	 PayPalOrderResponse orderResponse = response.getBody();
	 System.out.println("Order ID " + orderResponse.getId());
	 System.out.println("Validated: " + orderResponse.validate(orderId));
 }














}
