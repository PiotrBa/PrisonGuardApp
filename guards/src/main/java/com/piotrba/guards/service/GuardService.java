package com.piotrba.guards.service;

import com.piotrba.guards.dto.AssignRequest;
import com.piotrba.guards.dto.prisoner.PrisonerDTO;
import com.piotrba.guards.dto.visitor.VisitorDTO;
import com.piotrba.guards.entity.Guard;

import java.util.List;
import java.util.Optional;

public interface GuardService {
    List<Guard> findAllGuards();
    Optional<Guard> findGuardById(Long id);
    Guard registerNewGuard(Guard guard);
    Guard updateGuard(Long id, Guard guard);
    PrisonerDTO getPrisonerById(Long id);
    VisitorDTO getVisitorById(Long id);
    void assignPrisonerToVisitor(AssignRequest request);
    List<VisitorDTO> getVisitorsByPrisonerId(Long prisonerId);
}

