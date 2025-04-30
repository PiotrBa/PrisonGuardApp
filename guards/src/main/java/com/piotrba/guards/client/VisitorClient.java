package com.piotrba.guards.client;

import com.piotrba.guards.dto.visitor.VisitorDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "visitor-client", url = "http://localhost:8082/visitor")
public interface VisitorClient {

    @GetMapping("/all")
    List<VisitorDTO> getAllVisitors();

    @GetMapping("/{id}")
    VisitorDTO getVisitorById(@PathVariable Long id);

    @PostMapping("/add")
    VisitorDTO addVisitor(@RequestBody VisitorDTO visitorDTO);

    @PostMapping("/update/{id}")
    VisitorDTO updateVisitor(@PathVariable Long id, @RequestBody VisitorDTO visitorDTO);

    @DeleteMapping("/delete/{id}")
    void deleteVisitor(@PathVariable Long id);
}