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
            individual = setIndividual(individual, entity);
            individualList.add(individual);
        }

        return individualList;
    }
    

    public IndividualEntity getIndividual(String speciesCd, String id) {
        return individualMapper.findBySpeciesCdAndId(speciesCd, id);
    }

    public Individual createIndividual(Individual individual) {
        individualMapper.insert(individual);
        IndividualEntity entity = individualMapper.findBySpeciesCdAndId(individual.getSpeciesCd(), individual.getId());
        Individual model = new Individual();
        model = setIndividual(model, entity);

        return model;
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
