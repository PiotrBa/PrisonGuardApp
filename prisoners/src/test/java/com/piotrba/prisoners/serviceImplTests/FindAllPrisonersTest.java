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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindAllPrisonersTest {

    @Mock
    private PrisonersRepository prisonersRepository;

    @InjectMocks
    private PrisonerService prisonerService;

    List<Prisoner> prisonerList = new ArrayList<>(Arrays.asList(
            new Prisoner(1L, "John", "Doe", LocalDateTime.now(), LocalDateTime.now().plusYears(5), ImprisonmentRigour.MAXIMUM_SECURITY, new Address("123 Main St", "12345", "Springfield")),
            new Prisoner(2L, "Jane", "Doe", LocalDateTime.now(), LocalDateTime.now().plusYears(3), ImprisonmentRigour.MINIMUM_SECURITY, new Address("456 Elm St", "54321", "Shelbyville"))
    ));

    @Test
    public void findAllPrisoners_whenPrisonerExist_shouldReturnPrisonerList () {
        when(prisonersRepository.findAll()).thenReturn(prisonerList);
        List<Prisoner> result = prisonerService.findAll();
        assertEquals(prisonerList, result);
    }

    @Test
    public void findAllPrisoners_whenPrisonerDoNotExist_shouldReturnEmptyList() {
        when(prisonersRepository.findAll()).thenReturn(new ArrayList<>());
        List<Prisoner> result = prisonerService.findAll();
        assertTrue(result.isEmpty());
    }
}
