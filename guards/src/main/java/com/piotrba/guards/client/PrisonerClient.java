package com.piotrba.guards.client;

import com.piotrba.guards.dto.prisoner.PrisonerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "prisoner-client", url = "http://localhost:8081/prisoner")
public interface PrisonerClient {

    @GetMapping("/all")
    List<PrisonerDTO> getAllPrisoners();

    @GetMapping("/{id}")
    PrisonerDTO getPrisonerById(@PathVariable Long id);

    @PostMapping("/add")
    PrisonerDTO addPrisoner(@RequestBody PrisonerDTO prisonerDTO);

    @PostMapping("/update/{id}")
    PrisonerDTO updatePrisoner(@PathVariable Long id, @RequestBody PrisonerDTO prisonerDTO);

    @DeleteMapping("/delete/{id}")
    void deletePrisoner(@PathVariable Long id);
}