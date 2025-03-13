package com.piotrba.prisoners.serviceImplTests;

import com.piotrba.prisoners.entity.Prisoner;
import com.piotrba.prisoners.repo.PrisonersRepository;
import com.piotrba.prisoners.service.PrisonerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddPrisonerTest {

    @Mock
    private PrisonersRepository prisonersRepository;

    @InjectMocks
    private PrisonerService prisonerService;

    private Prisoner prisoner;
    private Prisoner existingPrisoner;

    @BeforeEach
    public void setUp() {
        prisoner = Prisoner.builder()
                .id(1L)
                .firstName("John")
                .lastName("Smith")
                .build();

        existingPrisoner = Prisoner.builder()
                .id(2L)
                .firstName("John")
                .lastName("Smith")
                .build();
    }

    @Test
    public void addNewPrisoner_withoutExistingFirstNameAndLastName_shouldAddNewPrisoner() {
        when(prisonersRepository.findByFirstNameAndLastName(prisoner.getFirstName(), prisoner.getLastName()))
                .thenReturn(Optional.empty());
        when(prisonersRepository.save(any(Prisoner.class)))
                .thenReturn(prisoner);
        Prisoner newPrisoner = prisonerService.addPrisoner(prisoner);
        assertEquals(prisoner.getFirstName(), newPrisoner.getFirstName());
        assertEquals(prisoner.getLastName(), newPrisoner.getLastName());
        verify(prisonersRepository, times(1)).save(any(Prisoner.class));
    }

    @Test
    public void addNewPrisoner_withExistingFirstNameAndLastName_shouldThrowException() {
        when(prisonersRepository.findByFirstNameAndLastName(prisoner.getFirstName(), prisoner.getLastName()))
                .thenReturn(Optional.of(existingPrisoner));
        try {
            prisonerService.addPrisoner(prisoner);
            fail("Expected an IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("Prisoner already exists with this name and last name", e.getMessage());
        }
        verify(prisonersRepository, never()).save(any(Prisoner.class));
    }
}
