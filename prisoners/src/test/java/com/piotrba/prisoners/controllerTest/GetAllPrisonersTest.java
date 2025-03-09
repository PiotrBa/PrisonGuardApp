package com.piotrba.prisoners.controllerTest;

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
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GetAllPrisonersTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PrisonersRepository prisonersRepository;

    @BeforeEach
    public void setUp() {
        prisonersRepository.deleteAll();

        List<Prisoner> prisoners = List.of(
                Prisoner.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .incarcerationDate(LocalDateTime.now().minusYears(2))
                        .imprisonmentEndDate(LocalDateTime.now().plusYears(3))
                        .imprisonmentRigour(ImprisonmentRigour.MEDIUM_SECURITY)
                        .address(new Address("123 Main St", "12345", "Springfield"))
                        .active(true)
                        .build(),
                Prisoner.builder()
                        .firstName("Steve")
                        .lastName("Smith")
                        .incarcerationDate(LocalDateTime.now().minusYears(1))
                        .imprisonmentEndDate(LocalDateTime.now().plusYears(5))
                        .imprisonmentRigour(ImprisonmentRigour.MAXIMUM_SECURITY)
                        .address(new Address("456 Elm St", "54321", "Shelbyville"))
                        .active(true)
                        .build(),
                Prisoner.builder()
                        .firstName("Emma")
                        .lastName("Williams")
                        .incarcerationDate(LocalDateTime.now().minusYears(3))
                        .imprisonmentEndDate(LocalDateTime.now().plusYears(2))
                        .imprisonmentRigour(ImprisonmentRigour.MINIMUM_SECURITY)
                        .address(new Address("789 Oak St", "67890", "Capital City"))
                        .active(true)
                        .build()
        );
        prisonersRepository.saveAll(prisoners);
    }

    @Test
    public void testGetAllPrisoners() throws Exception {
        mockMvc.perform(get("/prisoners")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[0].address.street", is("123 Main St")))
                .andExpect(jsonPath("$[1].firstName", is("Steve")))
                .andExpect(jsonPath("$[1].address.street", is("456 Elm St")))
                .andExpect(jsonPath("$[2].firstName", is("Emma")))
                .andExpect(jsonPath("$[2].address.street", is("789 Oak St")));
    }

    @Test
    public void testGetAllPrisoners_NoPrisonersFound() throws Exception {
        prisonersRepository.deleteAll();
        mockMvc.perform(get("/prisoners")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}