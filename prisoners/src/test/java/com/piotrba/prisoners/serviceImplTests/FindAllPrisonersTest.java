package com.piotrba.prisoners.serviceImplTests;

import com.piotrba.prisoners.entity.Address;
import com.piotrba.prisoners.entity.ImprisonmentRigour;
import com.piotrba.prisoners.entity.Prisoner;
import com.piotrba.prisoners.repo.PrisonersRepository;
import com.piotrba.prisoners.service.PrisonerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindAllPrisonersTest {

    @Mock
    private PrisonersRepository prisonersRepository;

    @InjectMocks
    private PrisonerService prisonerService;

    private List<Prisoner> prisonerList;

    @BeforeEach
    public void setUp() {
        prisonerList = List.of(
                Prisoner.builder()
                        .id(1L)
                        .firstName("John")
                        .lastName("Doe")
                        .incarcerationDate(LocalDateTime.now())
                        .imprisonmentEndDate(LocalDateTime.now().plusYears(5))
                        .imprisonmentRigour(ImprisonmentRigour.MAXIMUM_SECURITY)
                        .address(new Address("123 Main St", "12345", "Springfield"))
                        .active(true) // Dodajemy pole active
                        .build(),
                Prisoner.builder()
                        .id(2L)
                        .firstName("Jane")
                        .lastName("Doe")
                        .incarcerationDate(LocalDateTime.now())
                        .imprisonmentEndDate(LocalDateTime.now().plusYears(3))
                        .imprisonmentRigour(ImprisonmentRigour.MINIMUM_SECURITY)
                        .address(new Address("456 Elm St", "54321", "Shelbyville"))
                        .active(true)
                        .build()
        );
    }

    @Test
    public void findAllPrisoners_whenPrisonersExist_shouldReturnPrisonerList() {
        when(prisonersRepository.findAll()).thenReturn(prisonerList);
        List<Prisoner> result = prisonerService.findAll();
        assertEquals(prisonerList, result);
    }

    @Test
    public void findAllPrisoners_whenNoPrisonersExist_shouldReturnEmptyList() {
        when(prisonersRepository.findAll()).thenReturn(new ArrayList<>());
        List<Prisoner> result = prisonerService.findAll();
        assertTrue(result.isEmpty());
    }
}