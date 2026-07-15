package com.backend.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.backend.entity.SpeciesEntity;
import com.backend.mapper.SpeciesMapper;

@Service
public class SpeciesService {

    private static final String SYSTEM_USER = "system";

    private final SpeciesMapper speciesMapper;

    public SpeciesService(SpeciesMapper speciesMapper) {
        this.speciesMapper = speciesMapper;
    }

    public List<SpeciesEntity> getSpeciesList() {
        return speciesMapper.findAll();
    }

    public SpeciesEntity getSpecies(String speciesId) {
        SpeciesEntity species = speciesMapper.findBySpeciesId(requireNonBlank(speciesId, "species_id is required"));
        if (species == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "species not found");
        }
        return species;
    }

    @Transactional
    public SpeciesEntity createSpecies(SpeciesEntity request) {
        SpeciesEntity normalized = normalize(request);
        try {
            speciesMapper.insert(normalized);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "species already exists", e);
        }
        return getSpecies(normalized.getSpeciesId());
    }

    @Transactional
    public SpeciesEntity updateSpecies(String speciesId, SpeciesEntity request) {
        getSpecies(speciesId);

        SpeciesEntity normalized = normalize(request);
        int updated;
        try {
            updated = speciesMapper.update(speciesId, normalized);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "species update conflicts with existing data", e);
        }

        if (updated == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "species not found");
        }
        return getSpecies(normalized.getSpeciesId());
    }

    @Transactional
    public void deleteSpecies(String speciesId) {
        try {
            int deleted = speciesMapper.delete(requireNonBlank(speciesId, "species_id is required"));
            if (deleted == 0) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "species not found");
            }
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "species is referenced and cannot be deleted", e);
        }
    }

    private SpeciesEntity normalize(SpeciesEntity request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "request body is required");
        }

        SpeciesEntity normalized = new SpeciesEntity();
        normalized.setSpeciesId(requireNonBlank(request.getSpeciesId(), "species_id is required"));
        normalized.setJapaneseName(requireNonBlank(request.getJapaneseName(), "japanese_name is required"));
        normalized.setCommonName(trimToNull(request.getCommonName()));
        normalized.setEnglishName(trimToNull(request.getEnglishName()));
        normalized.setTotalLength(trimToNull(request.getTotalLength()));
        normalized.setBodyWeight(trimToNull(request.getBodyWeight()));
        normalized.setLifespan(trimToNull(request.getLifespan()));
        normalized.setCreateUser(SYSTEM_USER);
        normalized.setUpdateUser(SYSTEM_USER);
        return normalized;
    }

    private String requireNonBlank(String value, String message) {
        String normalized = trimToNull(value);
        if (normalized == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
        return normalized;
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
