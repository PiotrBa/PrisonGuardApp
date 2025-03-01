package com.piotrba.guards.serviceImplTests;

import com.piotrba.guards.client.PrisonerClient;
import com.piotrba.guards.client.VisitorClient;
import com.piotrba.guards.entity.Address;
import com.piotrba.guards.entity.Guard;
import com.piotrba.guards.repo.GuardsRepository;
import com.piotrba.guards.service.GuardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    @Mock
    private PrisonerClient prisonerClient;
    @Mock
    private VisitorClient visitorClient;

    private GuardService guardService;
    private List<Guard> guardsList;

    @BeforeEach
    public void setUp() {
        guardService = new GuardService(guardsRepository, prisonerClient, visitorClient);

        guardsList = List.of(
                Guard.builder()
                        .id(1L)
                        .firstName("John")
                        .lastName("Doe")
                        .phoneNumber("123456789")
                        .address(new Address("123 Main St", "12345", "Springfield"))
                        .email("john.doe@example.com")
                        .grantHighLevelAccess(true)
                        .active(true)
                        .build(),
                Guard.builder()
                        .id(2L)
                        .firstName("Steve")
                        .lastName("Smith")
                        .phoneNumber("123456789")
                        .address(new Address("456 Elm St", "54321", "Shelbyville"))
                        .email("steve.smith@example.com")
                        .grantHighLevelAccess(true)
                        .active(true)
                        .build(),
                Guard.builder()
                        .id(3L)
                        .firstName("Emma")
                        .lastName("Williams")
                        .phoneNumber("123456789")
                        .address(new Address("789 Oak St", "67890", "Capital City"))
                        .email("emma.williams@example.com")
                        .grantHighLevelAccess(true)
                        .active(true)
                        .build()
        );
    }

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