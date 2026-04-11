package com.backend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.backend.entity.IndividualEntity;
import com.backend.model.Individual;

@Mapper
public interface IndividualMapper {

    List<IndividualEntity> findAll();

    List<IndividualEntity> search(
        @Param("speciesId") String speciesId,
        @Param("idPrefix") String idPrefix,
        @Param("morph") String morph
    );

    IndividualEntity findBySpeciesIdAndId(@Param("speciesId") String speciesId, @Param("id") String id);

    List<String> findIdsByIdPrefix(@Param("idPrefix") String idPrefix);

    int insert(Individual individual);

    int update(@Param("speciesId") String speciesId, @Param("id") String id, @Param("individual") Individual individual);

    int delete(@Param("speciesId") String speciesId, @Param("id") String id);
}
