package com.backend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.backend.entity.IndividualEntity;
import com.backend.model.Individual;

@Mapper
public interface IndividualMapper {

    List<IndividualEntity> findAll();

    IndividualEntity findBySpeciesCdAndId(@Param("speciesCd") String speciesCd, @Param("id") String id);

    int insert(Individual individual);

    int update(@Param("speciesCd") String speciesCd, @Param("id") String id, @Param("individual") Individual individual);

    int delete(@Param("speciesCd") String speciesCd, @Param("id") String id);
}
