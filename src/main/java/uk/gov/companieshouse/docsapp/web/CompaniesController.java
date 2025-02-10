package uk.gov.companieshouse.docsapp.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.companieshouse.docsapp.dao.CompanyRegistry;
import uk.gov.companieshouse.docsapp.model.company.Company;
import uk.gov.companieshouse.docsapp.vo.CompanyListOptions;

import java.util.List;

@RestController
@RequestMapping("companies")
public class CompaniesController {
    private final static Logger logger = LoggerFactory.getLogger(CompaniesController.class);
    private final CompanyRegistry registry;

    public CompaniesController(CompanyRegistry registry) {
        this.registry = registry;
    }

    @GetMapping("")
    public ResponseEntity<List<Company>> getCompanies(CompanyListOptions options) {
        return ResponseEntity.ok(registry.getCompanies(options.getNamePattern(), options.getYearOfIncorporation(), options.getActiveState(), options.getCompanyType(), options.getSortBy()));
    }

    @GetMapping("{number}")
    public ResponseEntity<Company> getCompany(@PathVariable String number) {
        try {
            Company company = registry.getCompany(number);
            return ResponseEntity.ok(company);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Company not found")) {
                return ResponseEntity.notFound().build();
            } else {
                logger.error(e.getMessage(), e);
                return ResponseEntity.internalServerError().build();
            }
        }
    }

    @PostMapping("")
    public ResponseEntity<Company> addCompany(@RequestBody Company company) {
        return ResponseEntity.ok(registry.addCompany(company));
    }

    @PutMapping("{number}")
    public ResponseEntity<Void> changeCompany(@PathVariable String number, @RequestBody Company company) {
        try {
            registry.editCompany(number, company);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Company not found")) {
                return ResponseEntity.notFound().build();
            } else {
                logger.error(e.getMessage(), e);
                return ResponseEntity.internalServerError().build();
            }
        }
    }

    @PatchMapping("{number}")
    public ResponseEntity<Void> patchCompany(@PathVariable String number, @RequestBody Company company) {
        try {
            registry.patchCompany(number, company);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return switch (e.getMessage()) {
                case "Company not found" -> {
                    yield ResponseEntity.notFound().build();
                }
                case "Incompatible data type", "Invalid company type" -> {
                    yield ResponseEntity.badRequest().build();
                }
                default -> {
                    logger.error(e.getMessage(), e);
                    yield ResponseEntity.internalServerError().build();
                }
            };
        }
    }

    @DeleteMapping("{number}")
    public ResponseEntity<Void> strikeOffCompany(@PathVariable String number) {
        try {
            registry.deleteCompany(number);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Company not found")) {
                return ResponseEntity.notFound().build();
            } else {
                logger.error(e.getMessage(), e);
                return ResponseEntity.internalServerError().build();
            }
        }
    }

}
