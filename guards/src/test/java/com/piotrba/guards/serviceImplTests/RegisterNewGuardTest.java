package com.piotrba.guards.serviceImplTests;

import com.piotrba.guards.entity.Guard;
import com.piotrba.guards.repo.GuardsRepository;
import com.piotrba.guards.service.GuardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegisterNewGuardTest {

    @Mock
    private GuardsRepository guardsRepository;
    @InjectMocks
    private GuardService guardService;

    public Guard guard = Guard.builder()
            .id(1L)
            .firstName("Andy")
            .lastName("Smith")
            .email("andy.smith@example.com")
            .build();

    @Test
    public void registerNewGuard_withoutExistingEmail_shouldRegisterNewGuard() {
        when(guardsRepository.findByEmail(guard.getEmail())).thenReturn(Optional.empty());
        when(guardsRepository.save(guard)).thenReturn(guard);
        Guard registerNewGuard = guardService.registerNewGuard(guard);
        assertEquals(guard, registerNewGuard);
    }

    @Test
    public void registerNewGuard_withExistingEmail_shouldNotRegisterNewGuard() {
        when(guardsRepository.findByEmail(guard.getEmail())).thenReturn(Optional.of(guard));
        try {
            guardService.registerNewGuard(guard);
            fail("Expected an IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("Guard already exists with this email address", e.getMessage());
        }
    }

}
