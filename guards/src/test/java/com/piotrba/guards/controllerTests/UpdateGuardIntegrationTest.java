package com.piotrba.guards.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.piotrba.guards.entity.Address;
import com.piotrba.guards.entity.Guard;
import com.piotrba.guards.repo.GuardsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UpdateGuardIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GuardsRepository guardsRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Guard existingGuard;

    @BeforeEach
    public void setUp() {
        guardsRepository.deleteAll();

        Address address = new Address("123 Main St", "12345", "Springfield");
        existingGuard = new Guard(null, "John", "Doe", "123456789", address, "john.doe@example.com", true, true);
        existingGuard = guardsRepository.save(existingGuard);
    }

    @Test
    public void testUpdateGuard_GuardExists() throws Exception {
        Guard updatedGuard = new Guard(null, "Jane", "Doe", "987654321",
                new Address("456 Elm St", "54321", "Shelbyville"),
                "jane.doe@example.com", false, false);

        mockMvc.perform(post("/guard/update/{id}", existingGuard.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedGuard)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", is("Jane")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.phoneNumber", is("987654321")))
                .andExpect(jsonPath("$.address.firstLine", is("456 Elm St")))
                .andExpect(jsonPath("$.email", is("jane.doe@example.com")))
                .andExpect(jsonPath("$.grantHighLevelAccess", is(false)))
                .andExpect(jsonPath("$.active", is(false)));
    }

    @Test
    public void testUpdateGuard_GuardDoesNotExist() throws Exception {
        Guard updatedGuard = new Guard(null, "Jane", "Doe", "987654321",
                new Address("456 Elm St", "54321", "Shelbyville"),
                "jane.doe@example.com", false, false);

        mockMvc.perform(post("/guard/update/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedGuard)))
                .andExpect(status().isNotFound());
    }
}
