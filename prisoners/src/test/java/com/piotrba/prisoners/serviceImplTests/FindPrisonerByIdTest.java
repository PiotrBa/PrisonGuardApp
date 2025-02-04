package com.piotrba.prisoners.serviceImplTests;

import com.piotrba.prisoners.entity.Prisoner;
import com.piotrba.prisoners.repo.PrisonersRepository;
import com.piotrba.prisoners.service.PrisonerService;
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
public class FindPrisonerByIdTest {

    @Mock
    private PrisonersRepository prisonersRepository;
    @InjectMocks
    private PrisonerService prisonerService;

    public Prisoner prisoner = Prisoner.builder()
        .id(1L)
        .firstName("Emma")
        .lastName("Smith")
        .build();


    @Test
    public void findPrisonerById_whenPrisonsExist_shouldReturnPrisoner() {
        when(prisonersRepository.findById(1L)).thenReturn(Optional.of(prisoner));
        Optional<Prisoner> result = prisonerService.findById(1L);
        assertEquals(Optional.of(prisoner), result);
    }

    @Test
    public void findPrisonerById_whenPrisonerDoesNotExist_shouldNotReturnAnyPrisoner() {
        when(prisonersRepository.findById(2L)).thenReturn(Optional.empty());
        Optional<Prisoner> result = prisonerService.findById(2L);
        assertFalse(result.isPresent());
    }
}
