package com.piotrba.visitors.controllerTests;

import com.piotrba.visitors.entity.Address;
import com.piotrba.visitors.entity.Visitor;
import com.piotrba.visitors.entity.RelationshipToPrisoner;
import com.piotrba.visitors.repo.VisitorsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
public class GetVisitorIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VisitorsRepository visitorsRepository;

    @BeforeEach
    public void setUp() {
        visitorsRepository.deleteAll();

        Address address = new Address("123 Main St", "12345", "Springfield");
        Visitor visitor = new Visitor(null, "John", "Doe", "123456789", address, "john.doe@example.com", 1L, RelationshipToPrisoner.FRIEND);

        visitorsRepository.save(visitor);
    }

    @Test
    public void testGetVisitor() throws Exception {
        String email = "john.doe@example.com";

        mockMvc.perform(get("/visitor")
                        .param("email", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")))
                .andExpect(jsonPath("$.relationshipToPrisoner", is("FRIEND")));
    }

    @Test
    public void testGetVisitor_NotFound() throws Exception {
        String email = "nonexistent@example.com";

        mockMvc.perform(get("/visitor")
                        .param("email", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
