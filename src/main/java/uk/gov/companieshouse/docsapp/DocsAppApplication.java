package uk.gov.companieshouse.docsapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "uk.gov.companieshouse.docsapp.dao")
@EnableTransactionManagement
@EnableConfigurationProperties
public class DocsAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocsAppApplication.class, args);
	}

}
