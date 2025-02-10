package uk.gov.companieshouse.docsapp.model.filing;

public class AnnualReturn extends Filing {
    int activeDirectors;
    int numberOfShareholders;

    // Constructor
    public AnnualReturn(String companyName, int activeDirectors, int numberOfShareholders) {
        super("Annual Return", companyName);
        this.activeDirectors = activeDirectors;
        this.numberOfShareholders = numberOfShareholders;
    }

    @Override
    public void displayFilingDetails() {
        super.displayFilingDetails();
        System.out.println("Number of Shareholders: " + numberOfShareholders);
    }

}
