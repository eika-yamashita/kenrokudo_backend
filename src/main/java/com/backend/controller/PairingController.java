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

import com.backend.entity.PairingEntity;
import com.backend.service.PairingService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
public class PairingController {

    private final PairingService pairingService;

    public PairingController(PairingService pairingService) {
        this.pairingService = pairingService;
    }

    @GetMapping("/pairings")
    public ResponseEntity<List<PairingEntity>> getPairings() {
        return ResponseEntity.ok(pairingService.getPairings());
    }

    @GetMapping("/pairings/{species_id}/{fiscal_year}/{pairing_id}")
    public ResponseEntity<PairingEntity> getPairing(
        @PathVariable("species_id") String speciesId,
        @PathVariable("fiscal_year") Integer fiscalYear,
        @PathVariable("pairing_id") String pairingId
    ) {
        return ResponseEntity.ok(pairingService.getPairing(speciesId, fiscalYear, pairingId));
    }

    @PostMapping("/pairings")
    public ResponseEntity<PairingEntity> createPairing(@Valid @RequestBody PairingEntity pairing) {
        PairingEntity created = pairingService.createPairing(pairing);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/pairings/{species_id}/{fiscal_year}/{pairing_id}")
    public ResponseEntity<PairingEntity> updatePairing(
        @PathVariable("species_id") String speciesId,
        @PathVariable("fiscal_year") Integer fiscalYear,
        @PathVariable("pairing_id") String pairingId,
        @Valid @RequestBody PairingEntity pairing
    ) {
        PairingEntity updated = pairingService.updatePairing(speciesId, fiscalYear, pairingId, pairing);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/pairings/{species_id}/{fiscal_year}/{pairing_id}")
    public ResponseEntity<Void> deletePairing(
        @PathVariable("species_id") String speciesId,
        @PathVariable("fiscal_year") Integer fiscalYear,
        @PathVariable("pairing_id") String pairingId
    ) {
        pairingService.deletePairing(speciesId, fiscalYear, pairingId);
        return ResponseEntity.noContent().build();
    }
}
