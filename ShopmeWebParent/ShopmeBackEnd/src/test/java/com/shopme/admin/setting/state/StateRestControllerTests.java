package com.shopme.admin.setting.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;


@SpringBootTest
@AutoConfigureMockMvc
public class StateRestControllerTests {
	@Autowired
	private MockMvc mockMvc;
	@Autowired private ObjectMapper objectMapper;
	
	//@Autowired
	//private TestEntityManager entityManager;
	@Autowired 
	private CountryRepository countryRepo;
	@Autowired
	private StateRepository stateRepo;

	@Test
	@WithMockUser(username = "jinjuan", password = "2020", roles = "")
	public void testListByCountry() throws Exception {
		Integer countryId = 2;
		String url = "/states/list_by_country/" + countryId;
		
		MvcResult result = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn();

		String jsonResponse = result.getResponse().getContentAsString();
		//System.out.println("Response Result -> " + jsonResponse);
		
		 State[] states = objectMapper.readValue(jsonResponse, State[].class);
			
			  for(State state : states) { System.out.println(state); }
			 
		 
		 assertThat(states).hasSizeGreaterThan(2);	
	}
	
	@Test 
	@WithMockUser(username= "jinjuan", password ="2020", roles="")
	public void testCreateState() throws Exception {	
	  String url = "/states/save";
	  Integer countryId = 2;
	  Country country = countryRepo.findById(countryId).get();
	  
	  String stateName = "Qing Hai"; 
	  
	  State state = new State( stateName, country);
	  
		 MvcResult result =  mockMvc.perform(post(url).contentType("application/json")
				  .content(objectMapper.writeValueAsString(state))
				  .with(csrf()))
		          .andExpect(status().isOk() )
		          .andDo(print())
		          .andReturn();
		 String jsonResponse = result.getResponse().getContentAsString();
		 
		 System.out.println("new saved State  id = " + jsonResponse);
		 Integer savedStateId = Integer.parseInt(jsonResponse);
		 
		 Optional<State> stateObject = stateRepo.findById(savedStateId);
		 assertThat(stateObject.isPresent());
		 
		 State savedState = stateObject.get();  
		 
		 assertThat(savedState.getName()).isEqualTo(stateName);
	}
	
	@Test 
	@WithMockUser(username= "jinjuan", password ="2020", roles="")
	public void testUpdateState() throws Exception {	
	  String url = "/states/save";

	  Integer stateId = 14;
	  Optional<State> stateObject = stateRepo.findById(stateId);
	  
	  String newstateName = "Chang Chun";   
	  State state = stateObject.get();
	  state.setName(newstateName);
	  
		 MvcResult result =  mockMvc.perform(post(url).contentType("application/json")
				  .content(objectMapper.writeValueAsString(state))
				  .with(csrf()))
		          .andExpect(status().isOk() )
		          .andDo(print())
		          .andReturn();
		 String jsonResponse = result.getResponse().getContentAsString();
		 
		 System.out.println("new updated State  id = " + jsonResponse);
		 Integer updatedStateId = Integer.parseInt(jsonResponse);
		 

		 assertThat(updatedStateId == stateId).isTrue();
	}
	
	@Test 
	@WithMockUser(username= "jinjuan", password ="2020", roles="")
	public void testDeleteDate() throws Exception {
		Integer stateId = 13;
		String url = "/states/delete/"+ stateId;
		
		mockMvc.perform(get(url)).andExpect(status().isOk());
		 Optional<State> stateObject = stateRepo.findById(stateId);
		 
		 assertThat(stateObject.isPresent()).isFalse();
	}
}
