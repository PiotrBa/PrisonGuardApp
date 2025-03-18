package com.piotrba.guards.serviceImplTests;

import com.piotrba.guards.client.PrisonerClient;
import com.piotrba.guards.client.VisitorClient;
import com.piotrba.guards.entity.Guard;
import com.piotrba.guards.repo.GuardsRepository;
import com.piotrba.guards.service.GuardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GuardServiceTest {

    @Mock
    private GuardsRepository guardsRepository;
    @Mock
    private PrisonerClient prisonerClient;
    @Mock
    private VisitorClient visitorClient;
    @InjectMocks
    private GuardService guardService;

    @Test
    public void findAllGuard_whenGuardsExist_shouldReturnGuardList() {
        //given
        List<Guard> guardsList = List.of(
                Guard.builder()
                        .id(1L)
                        .build(),
                Guard.builder()
                        .id(2L)
                        .build()
        );
        when(guardsRepository.findAll()).thenReturn(guardsList);
        //when
        List<Guard> result = guardService.findAllGuards();
        //then
        assertEquals(guardsList, result);
        verify(guardsRepository).findAll();
    }

    @Test
    public void findAllGuard_whenGuardsDoNotExist_shouldReturnEmptyList() {
        //given
        when(guardsRepository.findAll()).thenReturn(Collections.emptyList());
        //when
        List<Guard> result = guardService.findAllGuards();
        //then
        assertTrue(result.isEmpty());
        verify(guardsRepository).findAll();
    }

    @Test
    public void findGuardById_whenGuardExists_shouldReturnGuard() {
        //given
        Guard guard = Guard.builder()
                .id(1L)
                .build();
        when(guardsRepository.findById(1L)).thenReturn(Optional.of(guard));
        //when
        Optional<Guard> result = guardService.findGuardById(1L);
        //then
        assertTrue(result.isPresent());
        assertEquals(guard, result.get());
        verify(guardsRepository).findById(1L);
    }

    @Test
    public void findGuardById_whenGuardDoesNotExist_shouldReturnEmptyOptional() {
        //given
        when(guardsRepository.findById(1L)).thenReturn(Optional.empty());
        //when
        Optional<Guard> result = guardService.findGuardById(1L);
        //then
        assertTrue(result.isEmpty());
        verify(guardsRepository).findById(1L);
    }

    @Test
    public void registerNewGuard_withoutExistingEmail_shouldRegisterNewGuard() {
        //given
        String email = "john.doe@example.com";
        Guard guard = Guard.builder()
                .email(email)
                .grantHighLevelAccess(true)
                .active(false)
                .build();
        Guard guardToSave = Guard.builder()
                .email(email)
                .grantHighLevelAccess(false)
                .active(true)
                .build();
        when(guardsRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(guardsRepository.save(guard)).thenReturn(guardToSave);
        //when
        Guard registeredGuard = guardService.registerNewGuard(guard);
        //then
        assertFalse(guard.getGrantHighLevelAccess());
        assertTrue(guard.getActive());
        assertEquals(guardToSave, registeredGuard);
        verify(guardsRepository).save(guard);
        verify(guardsRepository).findByEmail(email);
    }

    @Test
    public void registerNewGuard_whenGuardAllReadyExist_shouldNotRegisterNewGuard() {
        //given
        String email = "john.doe@example.com";
        Guard guard = Guard.builder()
                .email(email)
                .grantHighLevelAccess(true)
                .active(false)
                .build();
        when(guardsRepository.findByEmail(email)).thenReturn(Optional.of(guard));
        //when
        Exception exception = assertThrows(IllegalStateException.class, ()-> guardService.registerNewGuard(guard));
        //then
        assertTrue(guard.getGrantHighLevelAccess());
        assertFalse(guard.getActive());
        assertEquals("Guard already exists with this email address", exception.getMessage());
        verify(guardsRepository, never()).save(any());
        verify(guardsRepository).findByEmail(email);
    }
}
