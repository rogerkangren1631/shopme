package com.shopme.order;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderRestControllerTests {

	@Autowired private MockMvc mockMvc;
	@Autowired private ObjectMapper objectedMapper;
	
	@WithMockUser("rogerkangren@yahoo.com")
	@Test
	public void testSendOrderReturnRequestFailed() throws Exception {
		Integer orderId = 21;
		String reason = "I got wrong one"; 
		String note = "Please refund my money";
		OrderReturnRequest returnRequest
		 = new OrderReturnRequest (orderId, reason, note); 
		String requestURL = "/order/return"; 
		
//		mockMvc.perform(post(requestURL).with(csrf()));
//		.andExpect(MockMvcResultMatchers.status().isOk())
//		.andDo(print());
		
		mockMvc.perform(post(requestURL)
				.with(csrf())
				.contentType("application/json")
				.content(objectedMapper.writeValueAsString(returnRequest)))
		        .andExpect(status().isNotFound())
		        .andDo(print());					
	}
	
	@WithMockUser("brian.purcell3@gmail.com")
	@Test
	public void testSendOrderReturnRequestSuccess() throws Exception {
		Integer orderId = 6;
		String reason = "I got wrong one"; 
		String note = "Please refund my money";
		OrderReturnRequest returnRequest
		 = new OrderReturnRequest (orderId, reason, note); 
		String requestURL = "/order/return"; 
		
//		mockMvc.perform(post(requestURL).with(csrf()));
//		.andExpect(MockMvcResultMatchers.status().isOk())
//		.andDo(print());
		
		mockMvc.perform(post(requestURL)
				        .with(csrf())
				        .contentType("application/json")
				        .content(objectedMapper.writeValueAsString(returnRequest)))
		       .andExpect(status().isOk())
		       .andDo(print());					
	}
}
