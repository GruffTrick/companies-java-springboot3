package uk.gov.companieshouse.docsapp.model.filing;

public class ShareAllotment extends Filing {
    int sharesIssued;
    double sharePrice;

    // Constructor
    public ShareAllotment(String companyName, int sharesIssued, double sharePrice) {
        super("Share Allotment", companyName);
        this.sharesIssued = sharesIssued;
        this.sharePrice = sharePrice;
    }

    @Override
    public void displayFilingDetails() {
        super.displayFilingDetails();
        System.out.println("Shares Issued: " + sharesIssued);
        System.out.println("Share Price: £" + sharePrice);
    }

}
