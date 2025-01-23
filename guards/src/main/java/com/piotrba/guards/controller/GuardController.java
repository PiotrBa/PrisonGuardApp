package com.piotrba.guards.controller;

import com.piotrba.guards.dto.AssignRequest;
import com.piotrba.guards.dto.visitor.VisitorDTO;
import com.piotrba.guards.entity.Guard;
import com.piotrba.guards.exeptionHandler.GuardNotFoundException;
import com.piotrba.guards.service.GuardService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/guard")
@AllArgsConstructor
public class GuardController {

    private static final Logger logger = LoggerFactory.getLogger(GuardController.class);

    private final GuardService guardService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<Guard> getAllGuards() {
        logger.info("Received request to get all guards");
        return guardService.findAllGuards();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Guard getGuardById(@PathVariable Long id) {
        logger.info("Received request to get guard by id: {}", id);
        return guardService.findGuardById(id)
                .orElseThrow(() -> new GuardNotFoundException("Guard with id " + id + " not found"));
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Guard registerGuard(@RequestBody Guard guard) {
        logger.info("Received request to register a new guard");
        return guardService.registerNewGuard(guard);
    }

    @PostMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Guard updateGuard(@PathVariable Long id, @RequestBody Guard guard) {
        logger.info("Received request to update guard with id: {}", id);
        return guardService.updateGuard(id, guard);
    }

    @PostMapping("/assign-prisoner")
    @ResponseStatus(HttpStatus.OK)
    public String assignPrisonerToVisitor(@RequestBody AssignRequest request) {
        logger.info("Received request to assign prisoner with id: {} to visitor with id: {}",
                request.getPrisonerId(), request.getVisitorId());
        guardService.assignPrisonerToVisitor(request);
        return "Prisoner assigned to visitor successfully.";
    }

    @GetMapping("/prisoner/{id}/visitors")
    public List<VisitorDTO> getVisitorsByPrisonerId(@PathVariable Long id) {
        logger.info("Fetching visitors for prisoner with ID: {}", id);
        return guardService.getVisitorsByPrisonerId(id);
    }
}
