package com.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.entity.SpeciesEntity;
import com.backend.service.SpeciesService;

@RestController
@CrossOrigin(origins = "*")
public class SpeciesController {

    private final SpeciesService speciesService;

    public SpeciesController(SpeciesService speciesService) {
        this.speciesService = speciesService;
    }

    @GetMapping("/species")
    public ResponseEntity<List<SpeciesEntity>> getSpecies() {
        return ResponseEntity.ok(speciesService.getSpeciesList());
    }
}
