package com.backend.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.backend.entity.PairingEntity;

@Mapper
public interface PairingMapper {

    List<PairingEntity> findAll();

    PairingEntity findByPrimaryKey(
        @Param("speciesId") String speciesId,
        @Param("fiscalYear") Integer fiscalYear,
        @Param("pairingId") String pairingId
    );

    List<String> findPairingIdsBySpeciesAndYear(
        @Param("speciesId") String speciesId,
        @Param("fiscalYear") Integer fiscalYear
    );

    int countByPairingSignature(
        @Param("speciesId") String speciesId,
        @Param("maleParentId") String maleParentId,
        @Param("femaleParentId") String femaleParentId,
        @Param("pairingDate") Date pairingDate
    );

    int countByPairingSignatureExcludingPrimaryKey(
        @Param("speciesId") String speciesId,
        @Param("maleParentId") String maleParentId,
        @Param("femaleParentId") String femaleParentId,
        @Param("pairingDate") Date pairingDate,
        @Param("excludeSpeciesId") String excludeSpeciesId,
        @Param("excludeFiscalYear") Integer excludeFiscalYear,
        @Param("excludePairingId") String excludePairingId
    );

    int insert(PairingEntity pairing);

    int update(
        @Param("speciesId") String speciesId,
        @Param("fiscalYear") Integer fiscalYear,
        @Param("pairingId") String pairingId,
        @Param("pairing") PairingEntity pairing
    );

    int delete(
        @Param("speciesId") String speciesId,
        @Param("fiscalYear") Integer fiscalYear,
        @Param("pairingId") String pairingId
    );
}
