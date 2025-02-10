package uk.gov.companieshouse.docsapp.model.filing;

public class ChangeOfDirector extends Filing {
    String directorName;
    String changeType; // e.g., Appointment or Termination

    // Constructor
    public ChangeOfDirector(String companyName, String directorName, String changeType) {
        super("Change of Director", companyName);
        this.directorName = directorName;
        this.changeType = changeType;
    }

    @Override
    public void displayFilingDetails() {
        super.displayFilingDetails();
        System.out.println("Director Name: " + directorName);
        System.out.println("Change Type: " + changeType);
    }

}
