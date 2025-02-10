package uk.gov.companieshouse.docsapp.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import uk.gov.companieshouse.docsapp.dao.InMemoryCompanyRegistry;
import uk.gov.companieshouse.docsapp.model.company.Company;
import uk.gov.companieshouse.docsapp.model.company.LimitedCompany;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, properties = {"ukgov.companieshouse.docsapp.test=true"})
@AutoConfigureMockMvc
public class CompaniesControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private InMemoryCompanyRegistry registry;

    @BeforeEach
    public void setup() {
        registry.reset();
    }

    @Test
    public void testListCompanies() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/companies"))
                .andExpect(status().isOk())
                .andReturn();
        String json = result.getResponse().getContentAsString();
        List<Company> companies = mapper.readValue(json, new TypeReference<>() {});
        assertThat(companies.size()).isEqualTo(4);
        for (Company company : companies) {
            assertThat(registry.getCompany(company.getRegistrationNumber())).isNotNull();
        }
    }

    @Test
    public void testAddCompany() throws Exception {
        Company newCompany = new LimitedCompany("FedeSoft Ltd",true, 100);
        newCompany.setRegisteredAddress("666, On My Road, My City, EE10 3LX, UK");

        String json = mapper.writeValueAsString(newCompany);
        MvcResult result = this.mockMvc.perform(post("/companies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isOk())
                .andReturn();
        Company company = mapper.readValue(result.getResponse().getContentAsString(), LimitedCompany.class);
        Company verify = registry.getCompany(company.getRegistrationNumber());
        assertThat(verify).isNotNull();
        assertAll(
                () -> assertThat(company.getRegistrationNumber()).isNotNull(),
                () -> assertThat(verify.getCompanyName()).isEqualTo(newCompany.getCompanyName()),
                () -> assertThat(verify.getRegisteredAddress()).isEqualTo(newCompany.getRegisteredAddress()),
                () -> assertThat(verify.getIncorporatedOn()).isEqualTo(LocalDate.now())
        );
    }

    @Test
    public void testRemoveCompany() throws Exception {
        String id = "123456789";
        assumeThat(registry.getCompany(id)).isNotNull();
        this.mockMvc.perform(delete("/companies/{id}", id))
                .andExpect(status().isNoContent());
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> registry.getCompany(id));
        assertThat(e.getMessage()).isEqualTo("Company not found");
    }

    @Test
    public void testEditCompany() throws Exception {
        String id = "123456789";
        String newName = "New Company Name";
        Company company = registry.getCompany(id);
        assumeThat(company).isNotNull();
        assumeThat(company.getCompanyName()).isNotEqualTo(newName);
        company.setCompanyName(newName);
        mockMvc.perform(put("/companies/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(company))
                )
                .andExpect(status().isNoContent());
        assertThat(registry.getCompany(id).getCompanyName()).isEqualTo(newName);
    }

    @Test
    public void testPatchCompany() throws Exception {
        String id = "123456789";
        String newName = "New Company Name";
        Company company = registry.getCompany(id);
        assumeThat(company).isNotNull();
        assumeThat(company.getCompanyName()).isNotEqualTo(newName);
        Company newCompany = new LimitedCompany(newName,true, 100);
        newCompany.setActive(null);
        newCompany.setIncorporatedOn(null);
        newCompany.setRegistrationNumber(null);
        mockMvc.perform(patch("/companies/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newCompany))
                )
                .andExpect(status().isNoContent());
        assertAll(
                () -> assertThat(registry.getCompany(id).getCompanyName()).isEqualTo(newName),
                () -> assertThat(registry.getCompany(id).getRegistrationNumber()).isEqualTo(id),
                () -> assertThat(registry.getCompany(id).getIncorporatedOn()).isNotNull()
        );
    }

}
