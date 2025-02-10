package uk.gov.companieshouse.docsapp.model.company;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "registrationNumber")
public class LimitedCompany extends Company {
    private Integer numberOfShares;
    private Boolean plc;

    public LimitedCompany() {}

    public LimitedCompany(String companyName, Boolean active, Integer numberOfShares) {
        this(companyName, active, numberOfShares, false);
    }

    public LimitedCompany(String companyName, Boolean active, Integer numberOfShares, Boolean plc) {
        super(companyName, active);
        this.numberOfShares = numberOfShares;
        this.plc = plc;
    }

    public Integer getNumberOfShares() {
        return numberOfShares;
    }

    public void setNumberOfShares(int numberOfShares) {
        this.numberOfShares = numberOfShares;
    }

    public Boolean isPublic() {
        return plc;
    }

    public void setPublic(Boolean plc) {
        this.plc = plc;
    }

}
