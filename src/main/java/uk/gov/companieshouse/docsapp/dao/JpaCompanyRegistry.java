package uk.gov.companieshouse.docsapp.dao;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.docsapp.model.company.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "ukgov.companieshouse.docsapp.test", havingValue = "false", matchIfMissing = true)
public class JpaCompanyRegistry implements CompanyRegistry {

    private final Random companyNumberGenerator = new Random();
    private final CompanyRepository repository;

    public JpaCompanyRegistry(CompanyRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Company> getCompanies(String namePattern, Integer yearOfIncorporation, Boolean activeState, Type companyType, Sort sortBy) {
        org.springframework.data.domain.Sort sort = switch (sortBy) {
            case NUMBER -> org.springframework.data.domain.Sort.by("registrationNumber");
            case DATE -> org.springframework.data.domain.Sort.by("incorporatedOn");
            case NAME -> org.springframework.data.domain.Sort.by("companyName");
            case null -> null;
        };
        List<Company> companies = sort == null ? repository.findAll() : repository.findAll(sort);
        return companies.stream().filter(company -> {
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
                if (!companyClass.isAssignableFrom(Company.class)) keep = false;
            }
            return keep;
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Company getCompany(String number) {
        Optional<Company> company = repository.findById(number);
        if (company.isEmpty()) {
            throw new IllegalArgumentException("Company not found");
        } else {
            return company.get();
        }
    }

    @Override
    public void deleteCompany(String number) {
        if (!repository.existsById(number)) {
            throw new IllegalArgumentException("Company not found");
        } else {
            repository.deleteById(number);
        }
    }

    @Override
    public Company addCompany(Company data) {
        String number;
        do {
            number = String.valueOf(companyNumberGenerator.nextInt(100000000, 999999999 + 1));
        } while (repository.existsById(number));
        data.setRegistrationNumber(number);
        data.setIncorporatedOn(LocalDate.now());
        return repository.save(data);
    }

    @Override
    public void editCompany(String number, Company data) {
        if (!repository.existsById(number)) {
            throw new IllegalArgumentException("Company not found");
        }
        if (!number.equals(data.getRegistrationNumber())) data.setRegistrationNumber(number);
        repository.save(data);
    }

    @Override
    public void patchCompany(String number, Company data) {
        Optional<Company> maybeCompany = repository.findById(number);
        if (maybeCompany.isEmpty()) {
            throw new IllegalArgumentException("Company not found");
        } else {
            Company company = maybeCompany.get();
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
                case NonProfitOrganization nop -> {
                }
                default -> throw new IllegalArgumentException("Invalid company type");
            }
            repository.save(company);
        }
    }
}
