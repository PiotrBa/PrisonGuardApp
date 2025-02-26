package com.piotrba.prisoners.service;

import com.piotrba.prisoners.entity.Prisoner;
import com.piotrba.prisoners.repo.PrisonersRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PrisonerService {

    private static final Logger logger = LoggerFactory.getLogger(PrisonerService.class);

    private final PrisonersRepository prisonersRepository;

    public List<Prisoner> findAll() {
        logger.info("Fetching all prisoners");
        return prisonersRepository.findAll();
    }

    public Optional<Prisoner> findById(Long id) {
        logger.info("Fetching prisoner by ID: {}", id);
        return prisonersRepository.findById(id);
    }

    @Transactional
    public Prisoner updatePrisoner(Long id, Prisoner newPrisoner) {
        logger.info("Updating prisoner with ID: {}", id);
        Optional<Prisoner> existingPrisonerOptional = prisonersRepository.findById(id);
        if (existingPrisonerOptional.isEmpty()) {
            logger.error("Prisoner with ID {} does not exist", id);
            throw new IllegalArgumentException("Prisoner does not exist");
        }
        Prisoner existingPrisoner = existingPrisonerOptional.get();
        existingPrisoner.setFirstName(newPrisoner.getFirstName());
        existingPrisoner.setLastName(newPrisoner.getLastName());
        existingPrisoner.setIncarcerationDate(newPrisoner.getIncarcerationDate());
        existingPrisoner.setImprisonmentEndDate(newPrisoner.getImprisonmentEndDate());
        existingPrisoner.setImprisonmentRigour(newPrisoner.getImprisonmentRigour());
        existingPrisoner.setAddress(newPrisoner.getAddress());
        logger.info("Prisoner updated successfully: {}", existingPrisoner);
        return existingPrisoner;
    }

    @Transactional
    public void deletePrisoner(Long id) {
        logger.info("Releasing prisoner with ID: {}", id);
        Optional<Prisoner> prisonerOptional = prisonersRepository.findById(id);
        if (prisonerOptional.isEmpty()){
            logger.error("Prisoner with ID {} does not exist", id);
            throw new IllegalArgumentException("Prisoner does not exist");
        }
        Prisoner existingPrisoner = prisonerOptional.get();
        existingPrisoner.setActive(false);
        logger.info("Successful release of a prisoner: {}", existingPrisoner);
    }

    public Prisoner addPrisoner(Prisoner prisoner) {
        logger.info("Adding new prisoner: {}", prisoner);
        Prisoner savedPrisoner = prisonersRepository.save(prisoner);
        logger.info("Prisoner added successfully: {}", savedPrisoner);
        return savedPrisoner;
    }
}
