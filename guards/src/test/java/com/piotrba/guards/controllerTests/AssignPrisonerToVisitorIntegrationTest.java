package com.piotrba.guards.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.piotrba.guards.client.PrisonerClient;
import com.piotrba.guards.client.VisitorClient;
import com.piotrba.guards.dto.AssignRequest;
import com.piotrba.guards.dto.prisoner.PrisonerDTO;
import com.piotrba.guards.dto.visitor.VisitorDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AssignPrisonerToVisitorIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PrisonerClient prisonerClient;

    @MockBean
    private VisitorClient visitorClient;

    private final Long validVisitorId = 2L;
    private final Long validPrisonerId = 1L;
    private final Long invalidVisitorId = 999L;

    private VisitorDTO visitor;
    private PrisonerDTO prisoner;

    @BeforeEach
    public void setUp() {
        visitor = new VisitorDTO();
        visitor.setId(validVisitorId);
        visitor.setPrisonerIdNumber(null);
        visitor.setRelationshipToPrisoner(null);

        prisoner = new PrisonerDTO();
        prisoner.setId(validPrisonerId);
        prisoner.setFirstName("John");
        prisoner.setLastName("Doe");

        when(prisonerClient.getPrisonerById(validPrisonerId)).thenReturn(prisoner);
        when(visitorClient.getVisitorById(validVisitorId)).thenReturn(visitor);
    }

    @Test
    public void testAssignPrisonerToVisitor_Success() throws Exception {
        AssignRequest request = new AssignRequest(validVisitorId, validPrisonerId, "SIBLING");

        mockMvc.perform(post("/guard/assign-prisoner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Prisoner assigned to visitor successfully."));
        verify(visitorClient).updateVisitor(Mockito.eq(validVisitorId), Mockito.any(VisitorDTO.class));
    }

    @Test
    public void testAssignPrisonerToVisitor_PrisonerNotFound() throws Exception {
        AssignRequest request = new AssignRequest(validVisitorId, 999L, "SIBLING");

        when(prisonerClient.getPrisonerById(999L)).thenReturn(null);

        mockMvc.perform(post("/guard/assign-prisoner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAssignPrisonerToVisitor_VisitorNotFound() throws Exception {
        AssignRequest request = new AssignRequest(invalidVisitorId, validPrisonerId, "SIBLING");

        when(visitorClient.getVisitorById(invalidVisitorId)).thenReturn(null);

        mockMvc.perform(post("/guard/assign-prisoner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}