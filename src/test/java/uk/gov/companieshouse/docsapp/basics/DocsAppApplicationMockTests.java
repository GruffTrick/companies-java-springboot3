package uk.gov.companieshouse.docsapp.basics;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class DocsAppApplicationMockTests {

	@Autowired
	private HomeController controller;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

	@Test
	void greetingShouldReturnDefaultMessage() throws Exception {
		this.mockMvc.perform(get("/"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data").value("Hello World!"));
	}

	@Test
	void greetingShouldReturnDefaultMessage2() throws Exception {
		MvcResult mockResult = this.mockMvc.perform(get("/"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		RestResult restResult = mapper.readValue(mockResult.getResponse().getContentAsString(), RestResult.class);
		assertThat(restResult.getData()).isEqualTo("Hello World!");
	}

}
