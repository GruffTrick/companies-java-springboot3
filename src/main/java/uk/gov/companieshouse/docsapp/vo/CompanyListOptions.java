package uk.gov.companieshouse.docsapp.vo;

import uk.gov.companieshouse.docsapp.dao.CompanyRegistry;

public class CompanyListOptions {
    private String namePattern;
    private Integer yearOfIncorporation;
    private Boolean activeState;
    private CompanyRegistry.Type companyType;
    private CompanyRegistry.Sort sortBy;

    public String getNamePattern() {
        return namePattern;
    }

    public void setNamePattern(String namePattern) {
        this.namePattern = namePattern;
    }

    public Integer getYearOfIncorporation() {
        return yearOfIncorporation;
    }

    public void setYearOfIncorporation(Integer yearOfIncorporation) {
        this.yearOfIncorporation = yearOfIncorporation;
    }

    public Boolean getActiveState() {
        return activeState;
    }

    public void setActiveState(Boolean activeState) {
        this.activeState = activeState;
    }

    public CompanyRegistry.Type getCompanyType() {
        return companyType;
    }

    public void setCompanyType(CompanyRegistry.Type companyType) {
        this.companyType = companyType;
    }

    public CompanyRegistry.Sort getSortBy() {
        return sortBy;
    }

    public void setSortBy(CompanyRegistry.Sort sortBy) {
        this.sortBy = sortBy;
    }
}
