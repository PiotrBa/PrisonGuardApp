package com.piotrba.guards.serviceImplTests;

import com.piotrba.guards.entity.Address;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindAllGuardTest {

    @Mock
    private GuardsRepository guardsRepository;

    @InjectMocks
    private GuardService guardService;


    List<Guard> guardsList = List.of(
            new Guard(1L, "John", "Doe", "123456789", new Address("123 Main St", "12345", "Springfield"), "john.doe@example.com", true, true),
            new Guard(2L, "Steve", "Smith", "123456789", new Address("456 Elm St", "54321", "Shelbyville"), "steve.smith@example.com", true, true),
            new Guard(3L, "Emma", "Williams", "123456789", new Address("789 Oak St", "67890", "Capital City"), "emma.williams@example.com", true, true)
    );

    @Test
    public void findAllGuard_whenGuardsExist_shouldReturnGuardList() {
        when(guardsRepository.findAll()).thenReturn(guardsList);
        List<Guard> result = guardService.findAllGuards();
        assertEquals(guardsList, result);
    }

    @Test
    public void findAllGuard_whenGuardsDoNotExist_shouldReturnEmptyList() {
        when(guardsRepository.findAll()).thenReturn(Collections.emptyList());
        List<Guard> result = guardService.findAllGuards();
        assertTrue(result.isEmpty());
    }
}
