package com.piotrba.prisoners.controller;

import com.piotrba.prisoners.entity.Prisoner;
import com.piotrba.prisoners.exeptionHandler.PrisonerNotFoundException;
import com.piotrba.prisoners.service.PrisonerService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prisoner")
@AllArgsConstructor
public class PrisonerController {

    private static final Logger logger = LoggerFactory.getLogger(PrisonerController.class);

    private final PrisonerService prisonerService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<Prisoner> getAllPrisoners(){
        logger.info("Received request to get all prisoners");
        return prisonerService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Prisoner getPrisonerById(@PathVariable Long id){
        logger.info("Received request to get prisoner by id: {}", id);
        return prisonerService.findById(id)
                .orElseThrow(()-> new PrisonerNotFoundException("Prisoner with id " + id + " not found"));
    }

    @PostMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Prisoner updatePrisoner(@PathVariable Long id, @RequestBody Prisoner prisoner){
        logger.info("Received request to update prisoner with id: {}", id);
        return prisonerService.updatePrisoner(id, prisoner);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Prisoner addPrisoner(@RequestBody Prisoner prisoner) {
        logger.info("Received prisoner payload: {}", prisoner);
        return prisonerService.addPrisoner(prisoner);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePrisoner(@PathVariable Long id) {
        logger.info("Received request to release prisoner with id: {}", id);
        prisonerService.deletePrisoner(id);
    }
}