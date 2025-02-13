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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateGuardTest {

    @Mock
    private GuardsRepository guardsRepository;

    @InjectMocks
    private GuardService guardService;

    private final Guard existingGuard = Guard.builder()
            .id(1L)
            .firstName("Andy")
            .lastName("Smith")
            .email("andy.smith@example.com")
            .phoneNumber("+123456789")
            .build();

    private final Guard updatedGuard = Guard.builder()
            .id(1L)
            .firstName("Andrew")
            .lastName("Smithson")
            .email("andrew.smithson@example.com")
            .phoneNumber("+987654321")
            .build();

    @Test
    public void updateGuard_withValidId_shouldUpdateGuard() {
        when(guardsRepository.findById(existingGuard.getId())).thenReturn(Optional.of(existingGuard));
        Guard result = guardService.updateGuard(existingGuard.getId(), updatedGuard);
        assertNotNull(result);
        assertEquals(updatedGuard.getFirstName(), existingGuard.getFirstName());
        assertEquals(updatedGuard.getLastName(), existingGuard.getLastName());
        assertEquals(updatedGuard.getEmail(), existingGuard.getEmail());
        assertEquals(updatedGuard.getPhoneNumber(), existingGuard.getPhoneNumber());
        verify(guardsRepository, never()).save(any(Guard.class));
    }

    @Test
    public void updateGuard_withInvalidId_shouldThrowException() {
        when(guardsRepository.findById(999L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                guardService.updateGuard(999L, updatedGuard));
        assertEquals("Guard does not exist", exception.getMessage());
        verify(guardsRepository, never()).save(any(Guard.class));
    }
}
