package com.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.backend.entity.IndividualBloodlineEntity;

@Mapper
public interface IndividualBloodlineMapper {

    IndividualBloodlineEntity findByIndividualAndMorph(
        @Param("speciesId") String speciesId,
        @Param("individualId") String individualId,
        @Param("morphId") String morphId
    );

    int insert(IndividualBloodlineEntity bloodline);

    int deleteByIndividual(
        @Param("speciesId") String speciesId,
        @Param("individualId") String individualId
    );
}
