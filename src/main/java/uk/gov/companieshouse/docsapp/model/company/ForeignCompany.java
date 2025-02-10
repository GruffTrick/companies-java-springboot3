package uk.gov.companieshouse.docsapp.model.company;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "registrationNumber")
public class ForeignCompany extends Company {
    private String countryOfOrigin;

    public ForeignCompany() {}

    public ForeignCompany(String companyName, Boolean active, String countryOfOrigin) {
        super(companyName, active);
        this.countryOfOrigin = countryOfOrigin;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }
}
