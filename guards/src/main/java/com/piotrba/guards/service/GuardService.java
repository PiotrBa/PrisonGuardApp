package com.piotrba.guards.service;

import com.piotrba.guards.client.PrisonerClient;
import com.piotrba.guards.client.VisitorClient;
import com.piotrba.guards.dto.AssignRequest;
import com.piotrba.guards.dto.prisoner.PrisonerDTO;
import com.piotrba.guards.dto.visitor.RelationshipToPrisonerDTO;
import com.piotrba.guards.dto.visitor.VisitorDTO;
import com.piotrba.guards.entity.Guard;
import com.piotrba.guards.repo.GuardsRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GuardService {
    private static final Logger logger = LoggerFactory.getLogger(GuardService.class);

    private final GuardsRepository guardsRepository;
    private final PrisonerClient prisonerClient;
    private final VisitorClient visitorClient;

    public List<Guard> findAllGuards() {
        logger.info("Fetching all guards");
        return guardsRepository.findAll();
    }

    public Optional<Guard> findGuardById(Long id) {
        logger.info("Fetching guard by ID: {}", id);
        return guardsRepository.findById(id);
    }

    public Guard registerNewGuard(Guard guard) {
        logger.info("Registering new guard: {}", guard);
        Optional<Guard> existingGuard = guardsRepository.findByEmail(guard.getEmail());
        if (existingGuard.isPresent()) {
            logger.error("Guard with email {} already exists", guard.getEmail());
            throw new IllegalStateException("Guard already exists with this email address");
        }
        guard.setGrantHighLevelAccess(false);
        guard.setActive(true);
        Guard savedGuard = guardsRepository.save(guard);
        logger.info("Guard registered successfully: {}", savedGuard);
        return savedGuard;
    }

    @Transactional
    public Guard updateGuard(Long id, Guard newGuard) {
        logger.info("Updating guard with ID: {}", id);
        Optional<Guard> existingGuardOptional = guardsRepository.findById(id);
        if (existingGuardOptional.isEmpty()) {
            logger.error("Guard with ID {} does not exist", id);
            throw new IllegalArgumentException("Guard does not exist");
        }
        Guard existingGuard = existingGuardOptional.get();
        existingGuard.setFirstName(newGuard.getFirstName());
        existingGuard.setLastName(newGuard.getLastName());
        existingGuard.setEmail(newGuard.getEmail());
        existingGuard.setPhoneNumber(newGuard.getPhoneNumber());
        existingGuard.setAddress(newGuard.getAddress());
        existingGuard.setActive(newGuard.getActive());
        existingGuard.setGrantHighLevelAccess(newGuard.getGrantHighLevelAccess());
        logger.info("Guard updated successfully: {}", existingGuard);
        return existingGuard;
    }

    public PrisonerDTO getPrisonerById(Long id) {
        logger.info("Fetching prisoner by ID: {}", id);
        PrisonerDTO prisoner = prisonerClient.getPrisonerById(id);
        if (prisoner == null) {
            logger.error("Prisoner with ID {} not found", id);
            throw new IllegalArgumentException("Prisoner with ID " + id + " does not exist");
        }
        logger.info("Fetched prisoner: {}", prisoner);
        return prisoner;
    }

    public VisitorDTO getVisitorById(Long id) {
        logger.info("Fetching visitor by ID: {}", id);
        VisitorDTO visitor = visitorClient.getVisitorById(id);
        if (visitor == null) {
            logger.error("Visitor with ID {} not found", id);
            throw new IllegalArgumentException("Visitor with ID " + id + " does not exist");
        }
        logger.info("Fetched visitor: {}", visitor);
        return visitor;
    }

    @Transactional
    public void assignPrisonerToVisitor(AssignRequest request) {
        logger.info("Assigning prisoner with ID: {} to visitor with ID: {}", request.getPrisonerId(), request.getVisitorId());
        PrisonerDTO prisoner = prisonerClient.getPrisonerById(request.getPrisonerId());
        if (prisoner == null) {
            logger.error("Prisoner with ID {} does not exist", request.getPrisonerId());
            throw new IllegalArgumentException("Prisoner with ID " + request.getPrisonerId() + " does not exist");
        }
        VisitorDTO visitor = visitorClient.getVisitorById(request.getVisitorId());
        if (visitor == null) {
            logger.error("Visitor with ID {} does not exist", request.getVisitorId());
            throw new IllegalArgumentException("Visitor with ID " + request.getVisitorId() + " does not exist");
        }
        visitor.setPrisonerIdNumber(request.getPrisonerId());
        visitor.setRelationshipToPrisoner(RelationshipToPrisonerDTO.valueOf(request.getRelationshipToPrisoner()));
        visitorClient.updateVisitor(request.getVisitorId(), visitor);
        logger.info("Prisoner with ID {} assigned to visitor with ID {}", request.getPrisonerId(), request.getVisitorId());
    }

    public List<VisitorDTO> getVisitorsByPrisonerId(Long prisonerId) {
        logger.info("Fetching visitors assigned to prisoner with ID: {}", prisonerId);
        List<VisitorDTO> assignedVisitors = visitorClient.getAllVisitors()
                .stream()
                .filter(visitor -> visitor.getPrisonerIdNumber() != null && visitor.getPrisonerIdNumber().equals(prisonerId))
                .toList();
        if (assignedVisitors.isEmpty()) {
            logger.error("No visitors found for prisoner with ID: {}", prisonerId);
            throw new IllegalArgumentException("No visitors found for prisoner with ID " + prisonerId);
        }
        logger.info("Found {} visitors assigned to prisoner with ID: {}", assignedVisitors.size(), prisonerId);
        return assignedVisitors;
    }


}
