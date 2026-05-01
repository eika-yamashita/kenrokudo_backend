package com.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.backend.api.PairingsApi;
import com.backend.entity.PairingEntity;
import com.backend.service.PairingService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
public class PairingController implements PairingsApi {

    private final PairingService pairingService;

    public PairingController(PairingService pairingService) {
        this.pairingService = pairingService;
    }

    @Override
    public ResponseEntity<List<PairingEntity>> getPairings() {
        return ResponseEntity.ok(pairingService.getPairings());
    }

    @Override
    public ResponseEntity<List<PairingEntity>> searchPairings(
        String speciesId,
        Integer fiscalYear
    ) {
        return ResponseEntity.ok(pairingService.searchPairings(speciesId, fiscalYear));
    }

    @Override
    public ResponseEntity<PairingEntity> getPairing(
        String speciesId,
        Integer fiscalYear,
        String pairingId
    ) {
        return ResponseEntity.ok(pairingService.getPairing(speciesId, fiscalYear, pairingId));
    }

    @Override
    public ResponseEntity<PairingEntity> createPairing(@Valid PairingEntity pairing) {
        PairingEntity created = pairingService.createPairing(pairing);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<PairingEntity> updatePairing(
        String speciesId,
        Integer fiscalYear,
        String pairingId,
        @Valid PairingEntity pairing
    ) {
        PairingEntity updated = pairingService.updatePairing(speciesId, fiscalYear, pairingId, pairing);
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<Void> deletePairing(
        String speciesId,
        Integer fiscalYear,
        String pairingId
    ) {
        pairingService.deletePairing(speciesId, fiscalYear, pairingId);
        return ResponseEntity.noContent().build();
    }
}
