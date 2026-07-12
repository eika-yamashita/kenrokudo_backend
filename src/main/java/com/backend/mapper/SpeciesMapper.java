package com.backend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.backend.entity.SpeciesEntity;

@Mapper
public interface SpeciesMapper {
    List<SpeciesEntity> findAll();

    SpeciesEntity findBySpeciesId(@Param("speciesId") String speciesId);

    int insert(SpeciesEntity species);

    int update(@Param("speciesId") String speciesId, @Param("species") SpeciesEntity species);

    int delete(@Param("speciesId") String speciesId);
}
