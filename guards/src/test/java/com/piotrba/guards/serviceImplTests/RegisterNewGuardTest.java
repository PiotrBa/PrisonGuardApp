package com.piotrba.guards.serviceImplTests;

import com.piotrba.guards.entity.Guard;
import com.piotrba.guards.repo.GuardsRepository;
import com.piotrba.guards.service.GuardService;
import com.piotrba.guards.client.PrisonerClient;
import com.piotrba.guards.client.VisitorClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegisterNewGuardTest {

    @Mock
    private GuardsRepository guardsRepository;
    @Mock
    private PrisonerClient prisonerClient;
    @Mock
    private VisitorClient visitorClient;

    private GuardService guardService;

    @BeforeEach
    public void setUp() {
        guardService = new GuardService(guardsRepository, prisonerClient, visitorClient);
    }

    private final Guard guard = Guard.builder()
            .id(1L)
            .firstName("Andy")
            .lastName("Smith")
            .email("andy.smith@example.com")
            .build();

    private final Guard existingGuard = Guard.builder()
            .id(2L)
            .firstName("Mike")
            .lastName("Johnson")
            .email("andy.smith@example.com")
            .build();

    @Test
    public void registerNewGuard_withoutExistingEmail_shouldRegisterNewGuard() {
        when(guardsRepository.findByEmail(guard.getEmail())).thenReturn(Optional.empty());
        when(guardsRepository.save(guard)).thenReturn(guard);
        Guard registeredGuard = guardService.registerNewGuard(guard);
        assertEquals(guard, registeredGuard);
        verify(guardsRepository, times(1)).save(guard);
    }

    @Test
    public void registerNewGuard_withExistingEmail_shouldNotRegisterNewGuard() {
        when(guardsRepository.findByEmail(guard.getEmail())).thenReturn(Optional.of(existingGuard));
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            guardService.registerNewGuard(guard);
        });
        assertEquals("Guard already exists with this email address", exception.getMessage());
        verify(guardsRepository, never()).save(any(Guard.class));
    }

}
