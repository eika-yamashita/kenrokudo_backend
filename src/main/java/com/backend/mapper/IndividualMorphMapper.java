package com.backend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.backend.entity.IndividualMorphEntity;

@Mapper
public interface IndividualMorphMapper {

    List<IndividualMorphEntity> findByIndividual(
        @Param("speciesId") String speciesId,
        @Param("individualId") String individualId
    );

    int insert(IndividualMorphEntity entry);

    int deleteByIndividual(
        @Param("speciesId") String speciesId,
        @Param("individualId") String individualId
    );
}
