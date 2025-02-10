package uk.gov.companieshouse.docsapp.model.filing;

public class InsolvencyFiling extends Filing {
    String insolvencyType; // Liquidation, Administration, etc.

    // Constructor
    public InsolvencyFiling(String companyName, String insolvencyType) {
        super("Insolvency Filing", companyName);
        this.insolvencyType = insolvencyType;
    }

    @Override
    public void displayFilingDetails() {
        super.displayFilingDetails();
        System.out.println("Insolvency Type: " + insolvencyType);
    }
}
