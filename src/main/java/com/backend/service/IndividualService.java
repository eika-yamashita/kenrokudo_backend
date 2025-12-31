package com.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.backend.entity.Individual;
import com.backend.repository.IndividualRepository;

@Service
public class IndividualService {

    private final IndividualRepository individualRepository;

    public IndividualService(IndividualRepository individualRepository) {
        this.individualRepository = individualRepository;
    }

    public List<Individual> getIndividuals() {
        return individualRepository.findAll();
    }

    public Individual getIndividual(String speciesCd, String id) {
        return individualRepository.findBySpeciesCdAndId(speciesCd, id);
    }
}
