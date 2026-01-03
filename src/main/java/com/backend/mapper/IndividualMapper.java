package com.backend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.backend.entity.IndividualEntity;

@Mapper
public interface IndividualMapper {

    List<IndividualEntity> findAll();

    IndividualEntity findBySpeciesCdAndId(@Param("speciesCd") String speciesCd, @Param("id") String id);
}
