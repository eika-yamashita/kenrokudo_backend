package com.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.backend.entity.IndividualEntity;
import com.backend.mapper.IndividualMapper;
import com.backend.model.Individual;

@Service
public class IndividualService {

    private final IndividualMapper individualMapper;

    public IndividualService(IndividualMapper individualMapper) {
        this.individualMapper = individualMapper;
    }

    public List<Individual> getIndividuals() {
        List<IndividualEntity> entityList = individualMapper.findAll();
        List<Individual> individualList = new ArrayList<>();

        for (IndividualEntity entity : entityList) {
            Individual individual = new Individual();
            individual = setIndividual(individual, entity);
            individualList.add(individual);
        }

        return individualList;
    }

    public IndividualEntity getIndividual(String speciesCd, String id) {
        return individualMapper.findBySpeciesCdAndId(speciesCd, id);
    }

    public Individual getIndividualModel(String speciesCd, String id) {
        IndividualEntity entity = getIndividual(speciesCd, id);
        if (entity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "individual not found");
        }

        Individual model = new Individual();
        return setIndividual(model, entity);
    }

    public Individual createIndividual(Individual individual) {
        String speciesCd = individual.getSpeciesCd();
        String id = individual.getId();

        if (individualMapper.findBySpeciesCdAndId(speciesCd, id) != null) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                String.format("individual already exists (species_cd=%s, id=%s)", speciesCd, id)
            );
        }

        try {
            individualMapper.insert(individual);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                String.format("individual already exists (species_cd=%s, id=%s)", speciesCd, id),
                e
            );
        }

        IndividualEntity entity = individualMapper.findBySpeciesCdAndId(individual.getSpeciesCd(), individual.getId());
        Individual model = new Individual();
        model = setIndividual(model, entity);
        return model;
    }

    public void updateIndividual(String speciesCd, String id, Individual individual) {
        if (getIndividual(speciesCd, id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "individual not found");
        }

        int updated = individualMapper.update(speciesCd, id, individual);
        if (updated == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "individual not found");
        }
    }

    public void deleteIndividual(String speciesCd, String id) {
        int deleted = individualMapper.delete(speciesCd, id);
        if (deleted == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "individual not found");
        }
    }

    private Individual setIndividual(Individual model, IndividualEntity entity) {
        model.setSpeciesCd(entity.getSpeciesCd());
        model.setId(entity.getId());
        model.setMaleParentId(entity.getMaleParentId());
        model.setFemaleParentId(entity.getFemaleParentId());
        model.setMorph(entity.getMorph());
        model.setBloodline(entity.getBloodline());
        model.setGenderCategory(entity.getGenderCategory());
        model.setBreedingCategory(entity.getBreedingCategory());
        model.setBreeder(entity.getBreeder());
        model.setClutchDate(entity.getClutchDate());
        model.setHatchDate(entity.getHatchDate());
        model.setPurchaseFrom(entity.getPurchaseFrom());
        model.setPurchasePrice(entity.getPurchasePrice());
        model.setPurchaseDate(entity.getPurchaseDate());
        model.setSalesCategory(entity.getSalesCategory());
        model.setSalesTo(entity.getSalesTo());
        model.setSalesPriceTaxEx(entity.getSalesPriceTaxEx());
        model.setSalesPriceTax(entity.getSalesPriceTax());
        model.setSalesPriceTaxIn(entity.getSalesPriceTaxIn());
        model.setSalesDate(entity.getSalesDate());
        model.setDeathDate(entity.getDeathDate());
        model.setNote(entity.getNote());
        model.setCreateUser(entity.getCreateUser());
        model.setCreateAt(entity.getCreateAt());
        model.setUpdateUser(entity.getUpdateUser());
        model.setUpdateAt(entity.getUpdateAt());
        return model;
    }
}
