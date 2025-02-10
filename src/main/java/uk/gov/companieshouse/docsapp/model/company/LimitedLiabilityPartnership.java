package uk.gov.companieshouse.docsapp.model.company;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "registrationNumber")
public class LimitedLiabilityPartnership extends Company {
    private Integer numberOfPartners;

    public LimitedLiabilityPartnership() {}

    public LimitedLiabilityPartnership(String companyName, Boolean active, Integer numberOfPartners) {
        super(companyName, active);
        this.numberOfPartners = numberOfPartners;
    }

    public Integer getNumberOfPartners() {
        return numberOfPartners;
    }

    public void setNumberOfPartners(Integer numberOfPartners) {
        this.numberOfPartners = numberOfPartners;
    }
}
