package com.piotrba.guards.serviceImplTests;

import com.piotrba.guards.client.VisitorClient;
import com.piotrba.guards.dto.visitor.VisitorDTO;
import com.piotrba.guards.service.GuardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetVisitorsByPrisonerIdTest {

    @Mock
    private VisitorClient visitorClient;

    @InjectMocks
    private GuardService guardService;

    private final VisitorDTO visitor1 = VisitorDTO.builder()
            .id(1L)
            .firstName("Alice")
            .lastName("Johnson")
            .prisonerIdNumber(10L)
            .build();

    private final VisitorDTO visitor2 = VisitorDTO.builder()
            .id(2L)
            .firstName("Bob")
            .lastName("Smith")
            .prisonerIdNumber(10L)
            .build();

    private final VisitorDTO visitor3 = VisitorDTO.builder()
            .id(3L)
            .firstName("Charlie")
            .lastName("Brown")
            .prisonerIdNumber(15L)
            .build();

    @Test
    public void getVisitorsByPrisonerId_whenVisitorsExist_shouldReturnVisitorList() {
        when(visitorClient.getAllVisitors()).thenReturn(List.of(visitor1, visitor2, visitor3));
        List<VisitorDTO> result = guardService.getVisitorsByPrisonerId(10L);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(visitor1));
        assertTrue(result.contains(visitor2));
        assertFalse(result.contains(visitor3));
        verify(visitorClient, times(1)).getAllVisitors();
    }

    @Test
    public void getVisitorsByPrisonerId_whenNoVisitorsFound_shouldThrowException() {
        when(visitorClient.getAllVisitors()).thenReturn(List.of(visitor1, visitor2, visitor3));
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                guardService.getVisitorsByPrisonerId(20L));
        assertEquals("No visitors found for prisoner with ID 20", exception.getMessage());
        verify(visitorClient, times(1)).getAllVisitors();
    }
}