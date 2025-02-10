package uk.gov.companieshouse.docsapp;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "ukgov.companieshouse")
@Validated
public class DocsAppConfigProps {
    public static class DocsApp {
        private boolean test;
        @Min(100) @Max(199) private int secretCode;

        public boolean isTest() {
            return test;
        }

        public void setTest(boolean test) {
            this.test = test;
        }

        public int getSecretCode() {
            return secretCode;
        }

        public void setSecretCode(int secretCode) {
            this.secretCode = secretCode;
        }
    }

    private String service;
    private String developerName;
    @Valid private DocsApp docsapp;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getDeveloperName() {
        return developerName;
    }

    public void setDeveloperName(String developerName) {
        this.developerName = developerName;
    }

    public DocsApp getDocsapp() {
        return docsapp;
    }

    public void setDocsapp(DocsApp docsapp) {
        this.docsapp = docsapp;
    }

}
