package com.piotrba.guards.serviceImplTests;

import com.piotrba.guards.client.PrisonerClient;
import com.piotrba.guards.client.VisitorClient;
import com.piotrba.guards.dto.AssignRequest;
import com.piotrba.guards.dto.prisoner.PrisonerDTO;
import com.piotrba.guards.dto.visitor.RelationshipToPrisonerDTO;
import com.piotrba.guards.dto.visitor.VisitorDTO;
import com.piotrba.guards.service.GuardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AssignPrisonerToVisitorTest {

    @Mock
    private PrisonerClient prisonerClient;

    @Mock
    private VisitorClient visitorClient;

    @InjectMocks
    private GuardService guardService;

    private final PrisonerDTO prisoner = PrisonerDTO.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .build();

    private final VisitorDTO visitor = VisitorDTO.builder()
            .id(2L)
            .firstName("Alice")
            .lastName("Smith")
            .build();

    private final AssignRequest assignRequest = AssignRequest.builder()
            .prisonerId(1L)
            .visitorId(2L)
            .relationshipToPrisoner("FRIEND")
            .build();

    @Test
    public void assignPrisonerToVisitor_whenValidIds_shouldAssignSuccessfully() {
        when(prisonerClient.getPrisonerById(1L)).thenReturn(prisoner);
        when(visitorClient.getVisitorById(2L)).thenReturn(visitor);

        guardService.assignPrisonerToVisitor(assignRequest);

        assertEquals(1L, visitor.getPrisonerIdNumber());
        assertEquals(RelationshipToPrisonerDTO.FRIEND, visitor.getRelationshipToPrisoner());

        verify(visitorClient, times(1)).updateVisitor(2L, visitor);
    }

    @Test
    public void assignPrisonerToVisitor_whenPrisonerNotFound_shouldThrowException() {
        when(prisonerClient.getPrisonerById(1L)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                guardService.assignPrisonerToVisitor(assignRequest));

        assertEquals("Prisoner with ID 1 does not exist", exception.getMessage());
        verify(visitorClient, never()).updateVisitor(anyLong(), any(VisitorDTO.class));
    }

    @Test
    public void assignPrisonerToVisitor_whenVisitorNotFound_shouldThrowException() {
        when(prisonerClient.getPrisonerById(1L)).thenReturn(prisoner);
        when(visitorClient.getVisitorById(2L)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                guardService.assignPrisonerToVisitor(assignRequest));

        assertEquals("Visitor with ID 2 does not exist", exception.getMessage());
        verify(visitorClient, never()).updateVisitor(anyLong(), any(VisitorDTO.class));
    }
}
