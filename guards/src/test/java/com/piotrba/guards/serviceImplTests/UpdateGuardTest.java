package com.piotrba.guards.serviceImplTests;

import com.piotrba.guards.client.PrisonerClient;
import com.piotrba.guards.client.VisitorClient;
import com.piotrba.guards.entity.Guard;
import com.piotrba.guards.repo.GuardsRepository;
import com.piotrba.guards.service.GuardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateGuardTest {

    @Mock
    private GuardsRepository guardsRepository;
    @Mock
    private PrisonerClient prisonerClient;
    @Mock
    private VisitorClient visitorClient;

    private GuardService guardService;
    private Guard existingGuard;
    private Guard updatedGuard;

    @BeforeEach
    public void setUp() {
        guardService = new GuardService(guardsRepository, prisonerClient, visitorClient);

        existingGuard = Guard.builder()
                .id(1L)
                .firstName("Andy")
                .lastName("Smith")
                .email("andy.smith@example.com")
                .phoneNumber("+123456789")
                .active(true)
                .build();

        updatedGuard = Guard.builder()
                .id(1L)
                .firstName("Andrew")
                .lastName("Smithson")
                .email("andrew.smithson@example.com")
                .phoneNumber("+987654321")
                .active(true)
                .build();
    }

    @Test
    public void updateGuard_withValidId_shouldUpdateGuard() {
        when(guardsRepository.findById(existingGuard.getId())).thenReturn(Optional.of(existingGuard));
        Guard result = guardService.updateGuard(existingGuard.getId(), updatedGuard);
        System.out.println("Before update: " + existingGuard);
        System.out.println("After update: " + result);
        assertNotNull(result);
        assertEquals(updatedGuard.getFirstName(), result.getFirstName());
        assertEquals(updatedGuard.getLastName(), result.getLastName());
        assertEquals(updatedGuard.getEmail(), result.getEmail());
        assertEquals(updatedGuard.getPhoneNumber(), result.getPhoneNumber());
        verify(guardsRepository, never()).save(any(Guard.class));
    }

    @Test
    public void updateGuard_withInvalidId_shouldThrowException() {
        when(guardsRepository.findById(999L)).thenReturn(Optional.empty());
        try {
            guardService.updateGuard(999L, updatedGuard);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Guard does not exist", e.getMessage());
        }
        verify(guardsRepository, never()).save(any(Guard.class));
    }
}