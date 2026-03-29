package com.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.backend.api.IndividualsApi;
import com.backend.model.Individual;
import com.backend.service.IndividualService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
public class IndividualController implements IndividualsApi {

    private final IndividualService individualService;

    public IndividualController(IndividualService individualService) {
        this.individualService = individualService;
    }

    @Override
    public ResponseEntity<List<Individual>> individualsGet() {
        return ResponseEntity.ok(individualService.getIndividuals());
    }

    @GetMapping("/individuals/{species_cd}/{id}")
    public ResponseEntity<Individual> individualsSpeciesCdIdGet(
        @PathVariable("species_cd") String speciesCd,
        @PathVariable("id") String id
    ) {
        return ResponseEntity.ok(individualService.getIndividualModel(speciesCd, id));
    }

    @Override
    public ResponseEntity<Void> individualsSpeciesCdIdDelete(String speciesCd, String id) {
        individualService.deleteIndividual(speciesCd, id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Individual> individualsPost(@Valid Individual individual) {
        Individual created = individualService.createIndividual(individual);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<Void> individualsSpeciesCdIdPut(String speciesCd, String id, @Valid Individual individual) {
        individualService.updateIndividual(speciesCd, id, individual);
        return ResponseEntity.ok().build();
    }
}
