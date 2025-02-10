package uk.gov.companieshouse.docsapp.dao;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.docsapp.model.company.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "ukgov.companieshouse.docsapp.test", havingValue = "true", matchIfMissing = false)
public class InMemoryCompanyRegistry implements CompanyRegistry {
    final private HashMap<String, Company> companies = new HashMap<>();
    final private Random companyNumberGenerator = new Random();

    public InMemoryCompanyRegistry() {
        reset();
    }

    public void reset() {
        companies.clear();
        Object[][] companyData = new Object[][] {
                {"LTD", "My Company Ltd", "123456789", true, LocalDate.of(2010, 4, 20), 1000},
                {"FXC", "Italian Company Ltd", "637399827", true, LocalDate.of(2008, 1, 5), "Italy"},
                {"LLP", "We Are Partners LLP", "194745294", true, LocalDate.of(2023, 9, 1), 3},
                {"NPO", "No Profit Ltd", "946401763", true, LocalDate.of(2003, 3, 13)}
        };
        for (Object[] row : companyData) {
            Company company = switch ((String)row[0]) {
                case "LTD" -> new LimitedCompany((String)row[1], (boolean)row[3], (int)row[5]);
                case "FXC" -> new ForeignCompany((String)row[1], (boolean)row[3], (String)row[5]);
                case "LLP" -> new LimitedLiabilityPartnership((String)row[1], (boolean)row[3], (int)row[5]);
                case "NPO" -> new NonProfitOrganization((String)row[1], (boolean)row[3]);
                default -> throw new IllegalArgumentException("Invalid company type " + row[0]);
            };
            company.setRegistrationNumber((String)row[2]);
            company.setIncorporatedOn((LocalDate)row[4]);
            companies.put(company.getRegistrationNumber(), company);
        }
    }

    @Override
    public List<Company> getCompanies(String namePattern, Integer yearOfIncorporation, Boolean activeState, Type companyType, Sort sortBy) {
        List<Company> result = companies.values().stream().filter(company -> {
            boolean keep = true;
            if (namePattern != null && !company.getCompanyName().matches(namePattern)) keep = false;
            if (yearOfIncorporation != null && company.getIncorporatedOn().getYear() != yearOfIncorporation) keep = false;
            if (activeState != null && company.isActive() != activeState) keep = false;
            if (companyType != null) {
                Class<? extends Company> companyClass = switch (companyType) {
                    case LLP -> LimitedLiabilityPartnership.class;
                    case LTD -> LimitedCompany.class;
                    case FOREIGN -> ForeignCompany.class;
                    case NONPROFIT -> NonProfitOrganization.class;
                };
                if (!companyClass.isAssignableFrom(company.getClass())) keep = false;
            }
            return keep;
        }).collect(Collectors.toCollection(ArrayList::new));
        if (sortBy != null) switch (sortBy) {
            case NAME -> result.sort(Comparator.comparing(Company::getCompanyName));
            case DATE -> result.sort(Comparator.comparing(Company::getIncorporatedOn));
            case NUMBER -> result.sort(Comparator.comparing(Company::getRegistrationNumber));
        }
        return result;
    }

    @Override
    public Company getCompany(String number) {
        Company company = companies.get(number);
        if (company == null) {
            throw new IllegalArgumentException("Company not found");
        } else {
            return company;
        }
    }

    @Override
    public void deleteCompany(String number) {
        if (!companies.containsKey(number)) {
            throw new IllegalArgumentException("Company not found");
        } else {
            companies.remove(number);
        }
    }

    @Override
    public Company addCompany(Company data) {
        String number;
        do {
            number = String.valueOf(companyNumberGenerator.nextInt(100000000, 999999999 + 1));
        } while (companies.containsKey(number));
        data.setRegistrationNumber(number);
        data.setIncorporatedOn(LocalDate.now());
        companies.put(number, data);
        return data;
    }

    @Override
    public void editCompany(String number, Company data) {
        if (!companies.containsKey(number)) {
            throw new IllegalArgumentException("Company not found");
        }
        if (!number.equals(data.getRegistrationNumber())) data.setRegistrationNumber(number);
        companies.put(number, data);
    }

    @Override
    public void patchCompany(String number, Company data) {
        Company company = companies.get(number);
        if (company == null) {
            throw new IllegalArgumentException("Company not found");
        }
        if (!company.getClass().isAssignableFrom(data.getClass())) {
            throw new IllegalArgumentException("Incompatible data type");
        }
        if (data.getCompanyName() != null) company.setCompanyName(data.getCompanyName());
        if (data.isActive() != null) company.setActive(data.isActive());
        if (data.getIncorporatedOn() != null) company.setIncorporatedOn(data.getIncorporatedOn());
        if (data.getRegisteredAddress() != null) company.setRegisteredAddress(data.getRegisteredAddress());
        switch (company) {
            case ForeignCompany fc -> {
                fc.setCountryOfOrigin(((ForeignCompany)data).getCountryOfOrigin());
            }
            case LimitedCompany ltd -> {
                ltd.setNumberOfShares(((LimitedCompany)data).getNumberOfShares());
                ltd.setPublic(((LimitedCompany)data).isPublic());
            }
            case LimitedLiabilityPartnership llp -> {
                llp.setNumberOfPartners(((LimitedLiabilityPartnership)data).getNumberOfPartners());
            }
            case NonProfitOrganization ignored -> {
            }
            default -> throw new IllegalArgumentException("Invalid company type");
        }
    }

}
