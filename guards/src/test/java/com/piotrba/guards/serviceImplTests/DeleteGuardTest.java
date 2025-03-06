package com.piotrba.guards.serviceImplTests;

import com.piotrba.guards.client.PrisonerClient;
import com.piotrba.guards.client.VisitorClient;
import com.piotrba.guards.entity.Guard;
import com.piotrba.guards.repo.GuardsRepository;
import com.piotrba.guards.service.GuardService;
import org.junit.jupiter.api.BeforeEach;
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
    @Mock
    private PrisonerClient prisonerClient;
    @Mock
    private VisitorClient visitorClient;

    @InjectMocks
    private GuardService guardService;
    private Guard existingGuard;

    @BeforeEach
    public void setUp() {
        existingGuard = Guard.builder()
                .id(1L)
                .firstName("John")
                .lastName("Smith")
                .active(true)
                .build();
    }

    @Test
    public void findGuardById_whenGuardExists_shouldChangeActiveToFalse() {
        when(guardsRepository.findById(existingGuard.getId())).thenReturn(Optional.of(existingGuard));
        guardService.deleteGuard(existingGuard.getId());
        assertFalse(existingGuard.getActive());
        verify(guardsRepository, times(0)).save(any(Guard.class));
    }


    @Test
    public void findGuardById_whenGuardIsNotExist_shouldThrowException() {
        when(guardsRepository.findById(999L)).thenReturn(Optional.empty());
        try {
            guardService.deleteGuard(999L);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Guard does not exist", e.getMessage());
        }
        verify(guardsRepository, never()).save(any(Guard.class));
    }
}
