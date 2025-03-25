package com.piotrba.guards.serviceImplTests;

import com.piotrba.guards.client.PrisonerClient;
import com.piotrba.guards.client.VisitorClient;
import com.piotrba.guards.dto.AssignRequest;
import com.piotrba.guards.dto.prisoner.PrisonerDTO;
import com.piotrba.guards.dto.visitor.RelationshipToPrisonerDTO;
import com.piotrba.guards.dto.visitor.VisitorDTO;
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

    @Test
    public void updateGuard_withValidId_shouldUpdateGuard() {
        //given
        Guard existingGuard = Guard.builder()
                .id(1L)
                .firstName("Andy")
                .lastName("Smith")
                .email("andy.smith@example.com")
                .phoneNumber("+123456789")
                .active(true)
                .grantHighLevelAccess(false)
                .build();
       Guard updatedGuard = Guard.builder()
                .id(1L)
                .firstName("Andrew")
                .lastName("Smithson")
                .email("andrew.smithson@example.com")
                .phoneNumber("+987654321")
                .active(true)
               .grantHighLevelAccess(false)
               .build();

        when(guardsRepository.findById(existingGuard.getId())).thenReturn(Optional.of(existingGuard));
        //when
        Guard result = guardService.updateGuard(existingGuard.getId(), updatedGuard);
        //then
        assertNotNull(result);
        assertEquals(updatedGuard.getFirstName(), result.getFirstName());
        assertEquals(updatedGuard.getLastName(), result.getLastName());
        assertEquals(updatedGuard.getEmail(), result.getEmail());
        assertEquals(updatedGuard.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(updatedGuard.getActive(), result.getActive());
        assertEquals(updatedGuard.getGrantHighLevelAccess(), result.getGrantHighLevelAccess());

        verify(guardsRepository).findById(existingGuard.getId());
        verify(guardsRepository, never()).save(any(Guard.class));
    }

    @Test
    public void updateGuard_withInvalidId_shouldThrowException() {
        //given
        Long nonExistentId = 1L;
        Guard updatedGuard = Guard.builder()
                .id(nonExistentId)
                .firstName("Andy")
                .lastName("Smith")
                .email("andy.smith@example.com")
                .phoneNumber("+123456789")
                .active(true)
                .grantHighLevelAccess(false)
                .build();

        when(guardsRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        //when
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> guardService.updateGuard(nonExistentId, updatedGuard));

        //then
        assertEquals("Guard does not exist", e.getMessage());

        verify(guardsRepository).findById(nonExistentId);
        verify(guardsRepository, never()).save(any(Guard.class));
    }

    @Test
    public void getPrisonerById_withValidId_shouldReturnPrisoner() {
        //given
        PrisonerDTO existingPrisoner = PrisonerDTO.builder()
                .id(1L)
                .build();
        when(prisonerClient.getPrisonerById(existingPrisoner.getId())).thenReturn(existingPrisoner);
        //when
        PrisonerDTO result = guardService.getPrisonerById(existingPrisoner.getId());
        assertNotNull(result);
        assertEquals(existingPrisoner.getId(), result.getId());
        //then
        verify(prisonerClient, times(1)).getPrisonerById(existingPrisoner.getId());
    }

    @Test
    public void getPrisonerById_withInvalidId_shouldThrowException() {
        //given
        Long notExistingId = 1L;
        when(prisonerClient.getPrisonerById(notExistingId)).thenReturn(null);

        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> guardService.getPrisonerById(notExistingId));

        //then
        assertEquals("Prisoner with ID " + notExistingId + " does not exist", exception.getMessage());
        verify(prisonerClient, times(1)).getPrisonerById(notExistingId);
    }

    @Test
    public void getVisitorById_whenVisitorExists_shouldReturnVisitor() {
        //given
        VisitorDTO visitor = VisitorDTO.builder()
                .id(1L)
                .build();
        when(visitorClient.getVisitorById(1L)).thenReturn(visitor);
        //when
        VisitorDTO result = guardService.getVisitorById(1L);
        //then
        assertNotNull(result);
        assertEquals(visitor, result);
    }

    @Test
    public void getVisitorById_whenVisitorDoesNotExist_shouldThrowException() {
        //given
        Long notExistingId = 1L;
        when(visitorClient.getVisitorById(notExistingId)).thenReturn(null);
        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> guardService.getVisitorById(notExistingId));
        //then
        assertEquals("Visitor with ID " + notExistingId + " does not exist", exception.getMessage());
        verify(visitorClient, times(1)).getVisitorById(notExistingId);
    }

    @Test
    public void assignPrisonerToVisitor_whenValidIds_shouldAssignSuccessfully() {
        //given
        PrisonerDTO prisoner = PrisonerDTO.builder()
                .id(1L)
                .build();
        VisitorDTO visitor = VisitorDTO.builder()
                .id(2L)
                .build();
        AssignRequest assignRequest = AssignRequest.builder()
                .prisonerId(1L)
                .visitorId(2L)
                .relationshipToPrisoner("FRIEND")
                .build();
        when(prisonerClient.getPrisonerById(1L)).thenReturn(prisoner);
        when(visitorClient.getVisitorById(2L)).thenReturn(visitor);
        //when
        guardService.assignPrisonerToVisitor(assignRequest);
        //then
        assertEquals(1L, visitor.getPrisonerIdNumber());
        assertEquals(RelationshipToPrisonerDTO.FRIEND, visitor.getRelationshipToPrisoner());
        verify(visitorClient, times(1)).updateVisitor(2L, visitor);
    }

    @Test
    public void assignPrisonerToVisitor_whenPrisonerNotFound_shouldThrowException() {
        //given
        AssignRequest assignRequest = AssignRequest.builder()
                .prisonerId(1L)
                .visitorId(2L)
                .relationshipToPrisoner("FRIEND")
                .build();
        when(prisonerClient.getPrisonerById(1L)).thenReturn(null);
        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> guardService.assignPrisonerToVisitor(assignRequest));
        //then
        assertEquals("Prisoner with ID 1 does not exist", exception.getMessage());
        verify(visitorClient, never()).updateVisitor(anyLong(), any(VisitorDTO.class));
    }

    @Test
    public void assignPrisonerToVisitor_whenVisitorNotFound_shouldThrowException() {
        //given
        PrisonerDTO prisoner = PrisonerDTO.builder()
                .id(1L)
                .build();
        AssignRequest assignRequest = AssignRequest.builder()
                .prisonerId(1L)
                .visitorId(2L)
                .relationshipToPrisoner("FRIEND")
                .build();
        when(prisonerClient.getPrisonerById(1L)).thenReturn(prisoner);
        when(visitorClient.getVisitorById(2L)).thenReturn(null);
        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> guardService.assignPrisonerToVisitor(assignRequest));
        //then
        assertEquals("Visitor with ID 2 does not exist", exception.getMessage());
        verify(visitorClient, never()).updateVisitor(anyLong(), any(VisitorDTO.class));
    }

    @Test
    public void getVisitorsByPrisonerId_whenVisitorsExist_shouldReturnVisitorList() {
        //given
        VisitorDTO visitor1 = VisitorDTO.builder()
                .id(1L)
                .prisonerIdNumber(10L)
                .build();
        VisitorDTO visitor2 = VisitorDTO.builder()
                .id(2L)
                .prisonerIdNumber(10L)
                .build();
        VisitorDTO visitor3 = VisitorDTO.builder()
                .id(3L)
                .prisonerIdNumber(15L)
                .build();
        when(visitorClient.getAllVisitors()).thenReturn(List.of(visitor1, visitor2, visitor3));
        //when
        List<VisitorDTO> result = guardService.getVisitorsByPrisonerId(10L);
        //then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(visitor1));
        assertTrue(result.contains(visitor2));
        assertFalse(result.contains(visitor3));
        verify(visitorClient, times(1)).getAllVisitors();
    }

    @Test
    public void getVisitorsByPrisonerId_whenNoVisitorsFound_shouldThrowException() {
        //given
        VisitorDTO visitor1 = VisitorDTO.builder()
                .id(1L)
                .prisonerIdNumber(10L)
                .build();
        VisitorDTO visitor2 = VisitorDTO.builder()
                .id(2L)
                .prisonerIdNumber(10L)
                .build();
        VisitorDTO visitor3 = VisitorDTO.builder()
                .id(3L)
                .prisonerIdNumber(15L)
                .build();
        when(visitorClient.getAllVisitors()).thenReturn(List.of(visitor1, visitor2, visitor3));
        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> guardService.getVisitorsByPrisonerId(20L));
        //then
        assertEquals("No visitors found for prisoner with ID 20", exception.getMessage());
        verify(visitorClient, times(1)).getAllVisitors();
    }
}
