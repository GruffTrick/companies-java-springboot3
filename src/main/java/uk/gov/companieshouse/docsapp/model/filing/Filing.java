package uk.gov.companieshouse.docsapp.model.filing;

import java.time.LocalDateTime;

public class Filing {
    String filingType;
    String companyName;
    LocalDateTime filingDate;

    // Constructor
    public Filing(String filingType, String companyName) {
        this.filingType = filingType;
        this.companyName = companyName;
        this.filingDate = LocalDateTime.now();
    }

    // Display filing information
    public void displayFilingDetails() {
        System.out.println("Filing Type: " + filingType);
        System.out.println("Company Name: " + companyName);
    }
    
}
