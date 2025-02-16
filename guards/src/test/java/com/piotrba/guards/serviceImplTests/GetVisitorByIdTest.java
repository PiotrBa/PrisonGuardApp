package com.piotrba.guards.serviceImplTests;

import com.piotrba.guards.client.VisitorClient;
import com.piotrba.guards.dto.visitor.VisitorDTO;
import com.piotrba.guards.service.GuardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetVisitorByIdTest {

    @Mock
    private VisitorClient visitorClient;

    @InjectMocks
    private GuardService guardService;

    private final VisitorDTO visitor = VisitorDTO.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .phoneNumber("+123456789")
            .build();

    @Test
    public void getVisitorById_whenVisitorExists_shouldReturnVisitor() {
        when(visitorClient.getVisitorById(1L)).thenReturn(visitor);
        VisitorDTO result = guardService.getVisitorById(1L);
        assertNotNull(result);
        assertEquals(visitor, result);
    }

    @Test
    public void getVisitorById_whenVisitorDoesNotExist_shouldThrowException() {
        when(visitorClient.getVisitorById(2L)).thenReturn(null);
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                guardService.getVisitorById(2L));
        assertEquals("Visitor with ID 2 does not exist", exception.getMessage());
    }
}