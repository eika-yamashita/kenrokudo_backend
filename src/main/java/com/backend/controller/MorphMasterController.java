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

import com.backend.entity.MorphMasterEntity;
import com.backend.service.MorphMasterService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
public class MorphMasterController {

    private final MorphMasterService morphMasterService;

    public MorphMasterController(MorphMasterService morphMasterService) {
        this.morphMasterService = morphMasterService;
    }

    @GetMapping("/morphs")
    public ResponseEntity<List<MorphMasterEntity>> getMorphs() {
        return ResponseEntity.ok(morphMasterService.getMorphs());
    }

    @GetMapping("/morphs/search")
    public ResponseEntity<List<MorphMasterEntity>> searchMorphs(
        @RequestParam(value = "species_id", required = false) String speciesId
    ) {
        return ResponseEntity.ok(morphMasterService.searchMorphs(speciesId));
    }

    @GetMapping("/morphs/{species_id}/{morph_id}")
    public ResponseEntity<MorphMasterEntity> getMorph(
        @PathVariable("species_id") String speciesId,
        @PathVariable("morph_id") String morphId
    ) {
        return ResponseEntity.ok(morphMasterService.getMorph(speciesId, morphId));
    }

    @PostMapping("/morphs")
    public ResponseEntity<MorphMasterEntity> createMorph(@Valid @RequestBody MorphMasterEntity morph) {
        return ResponseEntity.status(HttpStatus.CREATED).body(morphMasterService.createMorph(morph));
    }

    @PutMapping("/morphs/{species_id}/{morph_id}")
    public ResponseEntity<MorphMasterEntity> updateMorph(
        @PathVariable("species_id") String speciesId,
        @PathVariable("morph_id") String morphId,
        @Valid @RequestBody MorphMasterEntity morph
    ) {
        return ResponseEntity.ok(morphMasterService.updateMorph(speciesId, morphId, morph));
    }

    @DeleteMapping("/morphs/{species_id}/{morph_id}")
    public ResponseEntity<Void> deleteMorph(
        @PathVariable("species_id") String speciesId,
        @PathVariable("morph_id") String morphId
    ) {
        morphMasterService.deleteMorph(speciesId, morphId);
        return ResponseEntity.noContent().build();
    }
}
