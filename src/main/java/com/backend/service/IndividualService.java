package com.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

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
        // DBから取得（Entity）
        List<IndividualEntity> entityList = individualMapper.findAll();

        // API用リストを初期化
        List<Individual> individualList = new ArrayList<>();

        // Entity → Model 変換
        for (IndividualEntity entity : entityList) {
            Individual individual = new Individual();

            individual.setSpeciesCd(entity.getSpeciesCd());
            individual.setId(entity.getId());
            individual.setMaleParentId(entity.getMaleParentId());
            individual.setFemaleParentId(entity.getFemaleParentId());
            individual.setMorph(entity.getMorph());
            individual.setBloodline(entity.getBloodline());
            individual.setGenderCategory(entity.getGenderCategory());
            individual.setBreedingCategory(entity.getBreedingCategory());
            individual.setBreeder(entity.getBreeder());
            individual.setClutchDate(entity.getClutchDate());
            individual.setHatchDate(entity.getHatchDate());
            individual.setPurchaseFrom(entity.getPurchaseFrom());
            individual.setPurchasePrice(entity.getPurchasePrice());
            individual.setPurchaseDate(entity.getPurchaseDate());
            individual.setSalesCategory(entity.getSalesCategory());
            individual.setSalesTo(entity.getSalesTo());
            individual.setSalesPriceTaxEx(entity.getSalesPriceTaxEx());
            individual.setSalesPriceTax(entity.getSalesPriceTax());
            individual.setSalesPriceTaxIn(entity.getSalesPriceTaxIn());
            individual.setSalesDate(entity.getSalesDate());
            individual.setDeathDate(entity.getDeathDate());
            individual.setNote(entity.getNote());
            individual.setCreateUser(entity.getCreateUser());
            individual.setCreateAt(entity.getCreateAt());
            individual.setUpdateUser(entity.getUpdateUser());
            individual.setUpdateAt(entity.getUpdateAt());

            individualList.add(individual);
        }

        return individualList;
    }
    

    public IndividualEntity getIndividual(String speciesCd, String id) {
        return individualMapper.findBySpeciesCdAndId(speciesCd, id);
    }
}
