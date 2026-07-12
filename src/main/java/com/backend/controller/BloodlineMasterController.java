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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.entity.BloodlineMasterEntity;
import com.backend.service.BloodlineMasterService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
public class BloodlineMasterController {

    private final BloodlineMasterService bloodlineMasterService;

    public BloodlineMasterController(BloodlineMasterService bloodlineMasterService) {
        this.bloodlineMasterService = bloodlineMasterService;
    }

    @GetMapping("/bloodlines")
    public ResponseEntity<List<BloodlineMasterEntity>> getBloodlines() {
        return ResponseEntity.ok(bloodlineMasterService.getBloodlines());
    }

    @GetMapping("/bloodlines/search")
    public ResponseEntity<List<BloodlineMasterEntity>> searchBloodlines(
        @RequestParam(value = "species_id", required = false) String speciesId,
        @RequestParam(value = "morph_id", required = false) String morphId
    ) {
        return ResponseEntity.ok(bloodlineMasterService.searchBloodlines(speciesId, morphId));
    }

    @GetMapping("/bloodlines/{species_id}/{morph_id}/{bloodline_id}")
    public ResponseEntity<BloodlineMasterEntity> getBloodline(
        @PathVariable("species_id") String speciesId,
        @PathVariable("morph_id") String morphId,
        @PathVariable("bloodline_id") String bloodlineId
    ) {
        return ResponseEntity.ok(bloodlineMasterService.getBloodline(speciesId, morphId, bloodlineId));
    }

    @PostMapping("/bloodlines")
    public ResponseEntity<BloodlineMasterEntity> createBloodline(
        @Valid @RequestBody BloodlineMasterEntity bloodline
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bloodlineMasterService.createBloodline(bloodline));
    }

    @PutMapping("/bloodlines/{species_id}/{morph_id}/{bloodline_id}")
    public ResponseEntity<BloodlineMasterEntity> updateBloodline(
        @PathVariable("species_id") String speciesId,
        @PathVariable("morph_id") String morphId,
        @PathVariable("bloodline_id") String bloodlineId,
        @Valid @RequestBody BloodlineMasterEntity bloodline
    ) {
        return ResponseEntity.ok(
            bloodlineMasterService.updateBloodline(speciesId, morphId, bloodlineId, bloodline)
        );
    }

    @DeleteMapping("/bloodlines/{species_id}/{morph_id}/{bloodline_id}")
    public ResponseEntity<Void> deleteBloodline(
        @PathVariable("species_id") String speciesId,
        @PathVariable("morph_id") String morphId,
        @PathVariable("bloodline_id") String bloodlineId
    ) {
        bloodlineMasterService.deleteBloodline(speciesId, morphId, bloodlineId);
        return ResponseEntity.noContent().build();
    }
}
