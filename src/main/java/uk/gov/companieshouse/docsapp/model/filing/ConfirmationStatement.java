package uk.gov.companieshouse.docsapp.model.filing;

public class ConfirmationStatement extends Filing {
    int activeDirectors;
    int numberOfShareholders;

    // Constructor
    public ConfirmationStatement(String companyName, int numberOfShareholders, int activeDirectors) {
        super("Confirmation Statement", companyName);
        this.activeDirectors = activeDirectors;
        this.numberOfShareholders = numberOfShareholders;
    }

    @Override
    public void displayFilingDetails() {
        super.displayFilingDetails();
        System.out.println("Number of Active Directors: " + activeDirectors);
    }
}
