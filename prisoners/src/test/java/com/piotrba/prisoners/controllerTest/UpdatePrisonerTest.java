package com.piotrba.prisoners.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.piotrba.prisoners.entity.Address;
import com.piotrba.prisoners.entity.ImprisonmentRigour;
import com.piotrba.prisoners.entity.Prisoner;
import com.piotrba.prisoners.repo.PrisonersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UpdatePrisonerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PrisonersRepository prisonersRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Prisoner existingPrisoner;

    @BeforeEach
    public void setUp() {
        prisonersRepository.deleteAll();

        Address address = new Address("123 Main St", "12345", "Springfield");
        existingPrisoner = Prisoner.builder()
                .firstName("John")
                .lastName("Doe")
                .incarcerationDate(LocalDateTime.now())
                .imprisonmentEndDate(LocalDateTime.now().plusYears(2))
                .imprisonmentRigour(ImprisonmentRigour.MEDIUM_SECURITY)
                .address(address)
                .active(true)
                .build();

        existingPrisoner = prisonersRepository.save(existingPrisoner);
    }

    @Test
    public void testUpdatePrisoner_PrisonerExists() throws Exception {
        Prisoner updatedPrisoner = Prisoner.builder()
                .firstName("Jane")
                .lastName("Doe")
                .incarcerationDate(LocalDateTime.now())
                .imprisonmentEndDate(LocalDateTime.now().plusYears(3))
                .imprisonmentRigour(ImprisonmentRigour.MAXIMUM_SECURITY)
                .address(new Address("456 Elm St", "54321", "Shelbyville"))
                .active(true)
                .build();

        mockMvc.perform(put("/prisoners/update/{id}", existingPrisoner.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPrisoner)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", is("Jane")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.imprisonmentRigour", is("MAXIMUM_SECURITY")))
                .andExpect(jsonPath("$.address.street", is("456 Elm St")));
    }

    @Test
    public void testUpdatePrisoner_PrisonerDoesNotExist() throws Exception {
        Prisoner updatedPrisoner = Prisoner.builder()
                .firstName("Jane")
                .lastName("Doe")
                .incarcerationDate(LocalDateTime.now())
                .imprisonmentEndDate(LocalDateTime.now().plusYears(3))
                .imprisonmentRigour(ImprisonmentRigour.MAXIMUM_SECURITY)
                .address(new Address("456 Elm St", "54321", "Shelbyville"))
                .active(true)
                .build();

        mockMvc.perform(put("/prisoners/update/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPrisoner)))
                .andExpect(status().isNotFound());
    }
}