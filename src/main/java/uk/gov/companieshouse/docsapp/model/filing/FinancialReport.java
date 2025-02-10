package uk.gov.companieshouse.docsapp.model.filing;

public class FinancialReport extends Filing {
    double profitAndLoss;
    double balanceSheet;

    FinancialReport(String companyName, double profitAndLoss, double balanceSheet) {
        super("FinancialReport", companyName);
        this.profitAndLoss = profitAndLoss;
        this.balanceSheet = balanceSheet;
    }

    public double getBalanceSheet() {
        return balanceSheet;
    }

    public double getProfitAndLoss() {
        return profitAndLoss;
    }
}
