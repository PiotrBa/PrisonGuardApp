package com.piotrba.guards.serviceImplTests;

import com.piotrba.guards.client.PrisonerClient;
import com.piotrba.guards.dto.prisoner.PrisonerDTO;
import com.piotrba.guards.service.GuardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetPrisonerByIdTest {

    @Mock
    private PrisonerClient prisonerClient;

    @InjectMocks
    private GuardService guardService;

    private final PrisonerDTO existingPrisoner = PrisonerDTO.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .build();

    @Test
    public void getPrisonerById_withValidId_shouldReturnPrisoner() {
        when(prisonerClient.getPrisonerById(existingPrisoner.getId())).thenReturn(existingPrisoner);
        PrisonerDTO result = guardService.getPrisonerById(existingPrisoner.getId());
        assertNotNull(result);
        assertEquals(existingPrisoner.getId(), result.getId());
        assertEquals(existingPrisoner.getFirstName(), result.getFirstName());
        assertEquals(existingPrisoner.getLastName(), result.getLastName());

        verify(prisonerClient, times(1)).getPrisonerById(existingPrisoner.getId());
    }

    @Test
    public void getPrisonerById_withInvalidId_shouldThrowException() {
        when(prisonerClient.getPrisonerById(999L)).thenReturn(null);
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                guardService.getPrisonerById(999L));
        assertEquals("Prisoner with ID 999 does not exist", exception.getMessage());
        verify(prisonerClient, times(1)).getPrisonerById(999L);
    }
}