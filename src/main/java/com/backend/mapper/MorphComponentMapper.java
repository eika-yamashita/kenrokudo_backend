package com.backend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.backend.entity.MorphComponentEntity;

@Mapper
public interface MorphComponentMapper {

    List<MorphComponentEntity> findByCombo(
        @Param("speciesId") String speciesId,
        @Param("comboMorphId") String comboMorphId
    );

    int insert(MorphComponentEntity component);

    int deleteByCombo(
        @Param("speciesId") String speciesId,
        @Param("comboMorphId") String comboMorphId
    );
}
