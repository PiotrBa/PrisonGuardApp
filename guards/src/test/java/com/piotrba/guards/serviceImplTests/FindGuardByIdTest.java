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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindGuardByIdTest {
    @Mock
    private GuardsRepository guardsRepository;
    @InjectMocks
    private GuardService guardService;
    public Guard guard = Guard.builder()
            .id(1L)
            .firstName("Andy")
            .lastName("Smith")
            .build();
    @Test
    public void findGuardById_whenGuardExists_shouldReturnGuard(){
        when(guardsRepository.findById(1L)).thenReturn(Optional.of(guard));
        Optional<Guard> result = guardService.findGuardById(1L);
        assertEquals(Optional.of(guard), result);
    }

    @Test
    public void findGuardById_whenGuardDoesNotExist_shouldReturnEmptyOptional(){
        when(guardsRepository.findById(2L)).thenReturn(Optional.empty());
        Optional<Guard> result = guardService.findGuardById(2L);
        assertFalse(result.isPresent());
    }

}
