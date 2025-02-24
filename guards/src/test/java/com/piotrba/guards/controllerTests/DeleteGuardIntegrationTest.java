package com.piotrba.guards.controllerTests;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DeleteGuardIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GuardsRepository guardsRepository;

    private Guard existingGuard;

    @BeforeEach
    public void setUp() {
        guardsRepository.deleteAll();

        Address address = new Address("123 Main St", "12345", "Springfield");
        existingGuard = new Guard(null, "John", "Doe", "123456789", address, "john.doe@example.com", true, true);
        existingGuard = guardsRepository.save(existingGuard);
    }

    @Test
    public void deleteGuard_whenGuardExists_shouldSetActiveToFalse() throws Exception {
        mockMvc.perform(post("/guard/delete/{id}", existingGuard.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(existingGuard.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is(existingGuard.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(existingGuard.getLastName())))
                .andExpect(jsonPath("$.active", is(false)));
    }

    @Test
    public void deleteGuard_whenGuardDoesNotExist_shouldReturnNotFound() throws Exception {
        mockMvc.perform(post("/guard/delete/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}