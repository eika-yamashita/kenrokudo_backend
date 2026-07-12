package com.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.backend.api.SpeciesApi;
import com.backend.entity.SpeciesEntity;
import com.backend.service.SpeciesService;

import jakarta.validation.Valid;

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

    @GetMapping("/species/{species_id}")
    public ResponseEntity<SpeciesEntity> getSpeciesDetail(@PathVariable("species_id") String speciesId) {
        return ResponseEntity.ok(speciesService.getSpecies(speciesId));
    }

    @PostMapping("/species")
    public ResponseEntity<SpeciesEntity> createSpecies(@Valid @RequestBody SpeciesEntity species) {
        return ResponseEntity.status(HttpStatus.CREATED).body(speciesService.createSpecies(species));
    }

    @PutMapping("/species/{species_id}")
    public ResponseEntity<SpeciesEntity> updateSpecies(
        @PathVariable("species_id") String speciesId,
        @Valid @RequestBody SpeciesEntity species
    ) {
        return ResponseEntity.ok(speciesService.updateSpecies(speciesId, species));
    }

    @DeleteMapping("/species/{species_id}")
    public ResponseEntity<Void> deleteSpecies(@PathVariable("species_id") String speciesId) {
        speciesService.deleteSpecies(speciesId);
        return ResponseEntity.noContent().build();
    }
}
