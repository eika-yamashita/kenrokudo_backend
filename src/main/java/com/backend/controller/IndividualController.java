package com.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.backend.entity.Individual;
import com.backend.service.IndividualService;

@RestController
@CrossOrigin(origins = "*")
public class IndividualController {

    private final IndividualService individualService;

    public IndividualController(IndividualService individualService) {
        this.individualService = individualService;
    }

    @GetMapping("/individuals")
    @ResponseBody
    public List<Individual> getIndividualInfo() {
        return individualService.getIndividuals();
    }

    // 1件取得
    @GetMapping("/individuals/{speciesCd}/{id}")
    public Individual getIndividual(@PathVariable  String speciesCd, @PathVariable String id) {
        return java.util.Optional.ofNullable(individualService.getIndividual(speciesCd, id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Individual not found"));
    }

    @PostMapping("/individuals")
    @ResponseStatus(HttpStatus.CREATED)
    public Individual createIndividual(@RequestBody Individual individual) {
        return individualService.createIndividual(individual);
    }
}
