package com.backend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.backend.entity.BloodlineMasterEntity;

@Mapper
public interface BloodlineMasterMapper {

    List<BloodlineMasterEntity> findAll();

    List<BloodlineMasterEntity> search(
        @Param("speciesId") String speciesId,
        @Param("morphId") String morphId
    );

    BloodlineMasterEntity findByPrimaryKey(
        @Param("speciesId") String speciesId,
        @Param("morphId") String morphId,
        @Param("bloodlineId") String bloodlineId
    );

    BloodlineMasterEntity findBySpeciesMorphAndBloodlineName(
        @Param("speciesId") String speciesId,
        @Param("morphId") String morphId,
        @Param("bloodlineName") String bloodlineName
    );

    List<String> findBloodlineIdsBySpeciesAndMorph(
        @Param("speciesId") String speciesId,
        @Param("morphId") String morphId
    );

    int insert(BloodlineMasterEntity bloodline);

    int update(
        @Param("speciesId") String speciesId,
        @Param("morphId") String morphId,
        @Param("bloodlineId") String bloodlineId,
        @Param("bloodline") BloodlineMasterEntity bloodline
    );

    int delete(
        @Param("speciesId") String speciesId,
        @Param("morphId") String morphId,
        @Param("bloodlineId") String bloodlineId
    );
}
