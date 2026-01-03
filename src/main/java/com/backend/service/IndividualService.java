package com.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.backend.entity.Individual;
import com.backend.mapper.IndividualMapper;

@Service
public class IndividualService {

    private final IndividualMapper individualMapper;

    public IndividualService(IndividualMapper individualMapper) {
        this.individualMapper = individualMapper;
    }

    public List<Individual> getIndividuals() {
        return individualMapper.findAll();
    }

    public Individual getIndividual(String speciesCd, String id) {
        return individualMapper.findBySpeciesCdAndId(speciesCd, id);
    }

    public Individual createIndividual(Individual individual) {
        individualMapper.insert(individual);
        return individualMapper.findBySpeciesCdAndId(individual.getSpeciesCd(), individual.getId());
    }
}
