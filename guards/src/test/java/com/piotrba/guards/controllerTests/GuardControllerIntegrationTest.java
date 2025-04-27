package com.piotrba.guards.controllerTests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.piotrba.guards.client.PrisonerClient;
import com.piotrba.guards.client.VisitorClient;
import com.piotrba.guards.dto.prisoner.PrisonerDTO;
import com.piotrba.guards.dto.visitor.VisitorDTO;
import com.piotrba.guards.entity.Address;
import com.piotrba.guards.entity.Guard;
import com.piotrba.guards.repo.GuardsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GuardControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GuardsRepository guardsRepository;

    @MockBean
    private PrisonerClient prisonerClient;

    @MockBean
    private VisitorClient visitorClient;


    @BeforeEach
    void setUp() {
        guardsRepository.deleteAll();
        List<Guard> guards = Arrays.asList(
                new Guard(null, "John", "Doe", "123456789", new Address("123 Main St", "12345", "Springfield"), "john.doe@example.com", true, true),
                new Guard(null, "Steve", "Smith", "123456789", new Address("456 Elm St", "54321", "Shelbyville"), "steve.smith@example.com", true, true),
                new Guard(null, "Emma", "Williams", "123456789", new Address("789 Oak St", "67890", "Capital City"), "emma.williams@example.com", true, true)
        );
        guardsRepository.saveAll(guards);
    }

    @Test
    void testGetAllGuards() throws Exception {
        MvcResult result = mockMvc.perform(get("/guard/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponse = result.getResponse().getContentAsString();
        String expectedResponse = Files.readString(Path.of("src/test/resources/ExpectedGuartList.json"));
        JSONAssert.assertEquals(expectedResponse, actualResponse, JSONCompareMode.LENIENT);
    }


    @Test
    void testGetGuardById() throws Exception {
        String expectedJson = Files.readString(Path.of("src/test/resources/ExpectedGuardJohn.json"));
        ObjectMapper mapper = new ObjectMapper();
        Guard guard = mapper.readValue(expectedJson, Guard.class);

        Guard saved = guardsRepository.save(guard);

        MvcResult result = mockMvc.perform(get("/guard/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode expectedJsonNode = mapper.readTree(expectedJson);
        ((ObjectNode) expectedJsonNode).put("id", saved.getId());

        String expectedResponse = mapper.writeValueAsString(expectedJsonNode);
        String actualResponse = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedResponse, actualResponse, JSONCompareMode.LENIENT);
    }

    @Test
    void testAddGuard() throws Exception {
        guardsRepository.deleteAll();

        String inputJson = Files.readString(Path.of("src/test/resources/ExpectedGuardJohn.json"));
        String expectedJson = Files.readString(Path.of("src/test/resources/ExpectedGuardJohnAfterSave.json"));

        MvcResult result = mockMvc.perform(post("/guard/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isCreated())
                .andReturn();


        String actualResponse = result.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualJsonNode = mapper.readTree(actualResponse);
        long actualId = actualJsonNode.get("id").asLong();

        JsonNode expectedJsonNode = mapper.readTree(expectedJson);
        ((ObjectNode) expectedJsonNode).put("id", actualId);
        JSONAssert.assertEquals(mapper.writeValueAsString(expectedJsonNode), actualResponse, JSONCompareMode.LENIENT
        );
    }

    @Test
    void testUpdateGuard() throws Exception {
        String originalJson = Files.readString(Path.of("src/test/resources/ExpectedGuardJohn.json"));
        String updatedJson = Files.readString(Path.of("src/test/resources/ExpectedGuardJohnAfterUpdate.json"));

        ObjectMapper mapper = new ObjectMapper();
        Guard guard = mapper.readValue(originalJson, Guard.class);
        Guard saved = guardsRepository.save(guard);


        MvcResult result = mockMvc.perform(post("/guard/update/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode expectedJsonNode = mapper.readTree(updatedJson);
        ((ObjectNode) expectedJsonNode).put("id", saved.getId());

        String expectedResponse = mapper.writeValueAsString(expectedJsonNode);
        String actualResponse = result.getResponse().getContentAsString();

        JSONAssert.assertEquals(expectedResponse, actualResponse, JSONCompareMode.LENIENT);
    }

    @Test
    void testDeleteGuard() throws Exception {
        // In the application, guard is not actually deleted from DB.
        // Instead, their status is set to inactive.

        String json = Files.readString(Path.of("src/test/resources/ExpectedGuardJohn.json"));
        ObjectMapper mapper = new ObjectMapper();
        Guard guard = mapper.readValue(json, Guard.class);
        Guard saved = guardsRepository.save(guard);

        mockMvc.perform(post("/guard/delete/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testAssignPrisonerToVisitor() throws Exception {
        String requestJson = Files.readString(Path.of("src/test/resources/ExpectedAssignRequest.json"));

        PrisonerDTO mockedPrisoner = PrisonerDTO.builder()
                .id(1L)
                .firstName("Mock")
                .lastName("Prisoner")
                .build();

        VisitorDTO mockedVisitor = VisitorDTO.builder()
                .id(2L)
                .firstName("Mock")
                .lastName("Visitor")
                .build();

        when(prisonerClient.getPrisonerById(1L)).thenReturn(mockedPrisoner);
        when(visitorClient.getVisitorById(2L)).thenReturn(mockedVisitor);
        when(visitorClient.updateVisitor(eq(2L), any(VisitorDTO.class)))
                .thenReturn(mockedVisitor);

        mockMvc.perform(post("/guard/assign-prisoner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Prisoner assigned to visitor successfully."));
    }

    @Test
    void testGetVisitorsByPrisonerId() throws Exception {
        Long prisonerId = 1L;

        VisitorDTO visitor1 = VisitorDTO.builder()
                .id(10L)
                .firstName("Emily")
                .lastName("Smith")
                .prisonerIdNumber(prisonerId)
                .build();

        VisitorDTO visitor2 = VisitorDTO.builder()
                .id(11L)
                .firstName("Michael")
                .lastName("Johnson")
                .prisonerIdNumber(prisonerId)
                .build();


        VisitorDTO unrelatedVisitor = VisitorDTO.builder()
                .id(12L)
                .firstName("Someone")
                .lastName("Else")
                .prisonerIdNumber(99L)
                .build();

        List<VisitorDTO> allVisitors = List.of(visitor1, visitor2, unrelatedVisitor);

        when(visitorClient.getAllVisitors()).thenReturn(allVisitors);

        MvcResult result = mockMvc.perform(get("/guard/prisoner/{id}/visitors", prisonerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String expectedJson = Files.readString(Path.of("src/test/resources/ExpectedVisitorsForPrisoner1.json"));
        String actualJson = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.LENIENT);
    }
}