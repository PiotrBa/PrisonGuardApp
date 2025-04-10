package com.piotrba.guards.controllerTests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

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
        ObjectMapper mapper = new ObjectMapper();
        Guard guard = mapper.readValue(originalJson, Guard.class);
        Guard saved = guardsRepository.save(guard);

        String updatedJson = Files.readString(Path.of("src/test/resources/ExpectedGuardJohnAfterUpdate.json"));

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

}
