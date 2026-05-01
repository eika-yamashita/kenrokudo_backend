package com.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.backend.api.SpeciesApi;
import com.backend.entity.SpeciesEntity;
import com.backend.service.SpeciesService;

@RestController
@CrossOrigin(origins = "*")
public class SpeciesController implements SpeciesApi {

    private final SpeciesService speciesService;

    public SpeciesController(SpeciesService speciesService) {
        this.speciesService = speciesService;
    }

    @Override
    public ResponseEntity<List<SpeciesEntity>> getSpecies() {
        return ResponseEntity.ok(speciesService.getSpeciesList());
    }
}
