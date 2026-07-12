package com.backend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.backend.entity.MorphMasterEntity;

@Mapper
public interface MorphMasterMapper {

    List<MorphMasterEntity> findAll();

    List<MorphMasterEntity> search(@Param("speciesId") String speciesId);

    MorphMasterEntity findByPrimaryKey(@Param("speciesId") String speciesId, @Param("morphId") String morphId);

    MorphMasterEntity findBySpeciesIdAndMorphName(@Param("speciesId") String speciesId, @Param("morphName") String morphName);

    List<MorphMasterEntity> findBySpeciesIdAndInheritanceCategory(
        @Param("speciesId") String speciesId,
        @Param("inheritanceCategory") String inheritanceCategory
    );

    List<String> findMorphIdsBySpeciesId(@Param("speciesId") String speciesId);

    int insert(MorphMasterEntity morph);

    int update(
        @Param("speciesId") String speciesId,
        @Param("morphId") String morphId,
        @Param("morph") MorphMasterEntity morph
    );

    int delete(@Param("speciesId") String speciesId, @Param("morphId") String morphId);
}
