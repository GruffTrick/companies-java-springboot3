package uk.gov.companieshouse.docsapp.basics;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DocsAppApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private HomeController controller;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

	@Test
	void greetingShouldReturnDefaultMessage() {
		RestResult<String> result = this.restTemplate.getForObject("http://localhost:" + port + "/", RestResult.class);
		assertThat(result.getStatus()).isEqualTo(RestResult.Status.SUCCESS);
		assertThat(result.getData()).isEqualTo("Hello World!");
	}
}
