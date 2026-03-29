package com.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.backend.entity.SpeciesEntity;
import com.backend.mapper.SpeciesMapper;

@Service
public class SpeciesService {

    private final SpeciesMapper speciesMapper;

    public SpeciesService(SpeciesMapper speciesMapper) {
        this.speciesMapper = speciesMapper;
    }

    public List<SpeciesEntity> getSpeciesList() {
        return speciesMapper.findAll();
    }
}
