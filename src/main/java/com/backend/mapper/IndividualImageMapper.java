package com.backend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.backend.entity.IndividualImageEntity;

@Mapper
public interface IndividualImageMapper {

    Long nextImageId();

    int countByIndividual(@Param("speciesId") String speciesId, @Param("individualId") String individualId);

    Integer nextSortOrder(@Param("speciesId") String speciesId, @Param("individualId") String individualId);

    int insert(IndividualImageEntity image);

    List<IndividualImageEntity> findByIndividual(@Param("speciesId") String speciesId, @Param("individualId") String individualId);

    IndividualImageEntity findByStoragePath(@Param("storagePath") String storagePath);

    IndividualImageEntity findByImageId(@Param("imageId") Long imageId);

    int clearPrimaryByIndividual(@Param("speciesId") String speciesId, @Param("individualId") String individualId);

    int setPrimaryByImageId(@Param("imageId") Long imageId, @Param("updatedBy") String updatedBy);

    int updateImageFile(
        @Param("imageId") Long imageId,
        @Param("storagePath") String storagePath,
        @Param("publicUrl") String publicUrl,
        @Param("fileName") String fileName,
        @Param("contentType") String contentType,
        @Param("fileSize") Long fileSize,
        @Param("isPrimary") Boolean isPrimary,
        @Param("updatedBy") String updatedBy
    );

    int deleteByImageId(@Param("imageId") Long imageId);
}
