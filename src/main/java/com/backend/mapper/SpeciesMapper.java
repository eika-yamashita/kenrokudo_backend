package com.backend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.backend.entity.SpeciesEntity;

@Mapper
public interface SpeciesMapper {
    List<SpeciesEntity> findAll();
}
