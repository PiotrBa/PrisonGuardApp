package com.piotrba.prisoners.serviceImplTests;

import com.piotrba.prisoners.entity.Address;
import com.piotrba.prisoners.entity.ImprisonmentRigour;
import com.piotrba.prisoners.entity.Prisoner;
import com.piotrba.prisoners.repo.PrisonersRepository;
import com.piotrba.prisoners.service.PrisonerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdatePrisonerTest {

    @Mock
    private PrisonersRepository prisonersRepository;

    @InjectMocks
    private PrisonerService prisonerService;
    Prisoner prisoner = Prisoner.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .incarcerationDate(LocalDateTime.now())
            .imprisonmentEndDate(LocalDateTime.now().plusYears(5))
            .imprisonmentRigour(ImprisonmentRigour.MAXIMUM_SECURITY)
            .address(new Address("123 Main St", "12345", "Springfield"))
            .build();
    Prisoner updatedPrisoner = Prisoner.builder()
            .id(1L)
            .firstName("John")
            .lastName("Smith")
            .incarcerationDate(LocalDateTime.now())
            .imprisonmentEndDate(LocalDateTime.now().plusYears(5))
            .imprisonmentRigour(ImprisonmentRigour.MAXIMUM_SECURITY)
            .address(new Address("123 Main St", "12345", "Springfield"))
            .build();

    @Test
    public void updatePrisoner_whenPrisonerExists_shouldUpdatePrisoner() {
        when(prisonersRepository.findById(1L)).thenReturn(Optional.of(prisoner));
        Prisoner result = prisonerService.updatePrisoner(1L, updatedPrisoner);
        assertEquals(updatedPrisoner.getLastName(), result.getLastName());
        assertEquals(updatedPrisoner.getFirstName(), result.getFirstName());
    }

    @Test
    public void updatePrisoner_whenPrisonerDoesNotExist_shouldThrowException() {
        when(prisonersRepository.findById(1L)).thenReturn(Optional.empty());
        try {
            prisonerService.updatePrisoner(1L, updatedPrisoner);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Prisoner does not exist", e.getMessage());
        }
    }
}
