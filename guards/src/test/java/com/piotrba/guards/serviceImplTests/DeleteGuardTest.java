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
public class DeleteGuardTest {

    @Mock
    private GuardsRepository guardsRepository;

    @InjectMocks
    private GuardService guardService;
    public Guard existingGuard = Guard.builder()
            .id(1L)
            .firstName("John")
            .lastName("Smith")
            .active(true)
            .build();

    @Test
    public void findGuardById_whenGuardExist_shouldChangeActiveForFalse() {
        when(guardsRepository.findById(existingGuard.getId())).thenReturn(Optional.of(existingGuard));

        Guard result = guardService.deleteGuard(existingGuard.getId());

        assertNotNull(result);
        assertEquals(existingGuard.getFirstName(), result.getFirstName());
        assertEquals(existingGuard.getLastName(), result.getLastName());
        assertFalse(result.getActive());

        verify(guardsRepository, never()).save(any(Guard.class));
    }

    @Test
    public void findGuardById_whenGuardIsNotExist_shouldThrowException(){
        when(guardsRepository.findById(999L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(IllegalArgumentException.class, ()-> guardService.deleteGuard(999L));
        assertEquals("Guard does not exist", exception.getMessage());
        verify(guardsRepository, never()).save(any(Guard.class));
    }
}
