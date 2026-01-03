package com.backend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.backend.entity.Individual;

@Mapper
public interface IndividualMapper {

    List<Individual> findAll();

    Individual findBySpeciesCdAndId(@Param("speciesCd") String speciesCd, @Param("id") String id);

    int insert(Individual individual);
}
