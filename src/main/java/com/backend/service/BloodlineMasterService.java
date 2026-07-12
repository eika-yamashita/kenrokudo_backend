package com.backend.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.backend.entity.BloodlineMasterEntity;
import com.backend.mapper.BloodlineMasterMapper;

@Service
public class BloodlineMasterService {

    private final BloodlineMasterMapper bloodlineMasterMapper;

    public BloodlineMasterService(BloodlineMasterMapper bloodlineMasterMapper) {
        this.bloodlineMasterMapper = bloodlineMasterMapper;
    }

    public List<BloodlineMasterEntity> getBloodlines() {
        return bloodlineMasterMapper.findAll();
    }

    public List<BloodlineMasterEntity> searchBloodlines(String speciesIdRaw, String morphIdRaw) {
        return bloodlineMasterMapper.search(trimToNull(speciesIdRaw), trimToNull(morphIdRaw));
    }

    public BloodlineMasterEntity getBloodline(String speciesId, String morphId, String bloodlineId) {
        BloodlineMasterEntity bloodline = bloodlineMasterMapper.findByPrimaryKey(
            requireNonBlank(speciesId, "species_id is required"),
            requireNonBlank(morphId, "morph_id is required"),
            requireNonBlank(bloodlineId, "bloodline_id is required")
        );
        if (bloodline == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "bloodline not found");
        }
        return bloodline;
    }

    public BloodlineMasterEntity getBloodlineByName(String speciesId, String morphId, String bloodlineName) {
        if (trimToNull(speciesId) == null || trimToNull(morphId) == null || trimToNull(bloodlineName) == null) {
            return null;
        }
        return bloodlineMasterMapper.findBySpeciesMorphAndBloodlineName(speciesId, morphId, bloodlineName);
    }

    @Transactional
    public BloodlineMasterEntity createBloodline(BloodlineMasterEntity request) {
        BloodlineMasterEntity normalized = normalizeForCreate(request);
        try {
            bloodlineMasterMapper.insert(normalized);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "bloodline already exists", e);
        }
        return getBloodline(normalized.getSpeciesId(), normalized.getMorphId(), normalized.getBloodlineId());
    }

    @Transactional
    public BloodlineMasterEntity updateBloodline(
        String speciesId,
        String morphId,
        String bloodlineId,
        BloodlineMasterEntity request
    ) {
        getBloodline(speciesId, morphId, bloodlineId);
        BloodlineMasterEntity normalized = normalizeForUpdate(request);
        try {
            int updated = bloodlineMasterMapper.update(speciesId, morphId, bloodlineId, normalized);
            if (updated == 0) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "bloodline not found");
            }
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "bloodline update conflicts with existing data", e);
        }
        return getBloodline(normalized.getSpeciesId(), normalized.getMorphId(), normalized.getBloodlineId());
    }

    @Transactional
    public void deleteBloodline(String speciesId, String morphId, String bloodlineId) {
        try {
            int deleted = bloodlineMasterMapper.delete(
                requireNonBlank(speciesId, "species_id is required"),
                requireNonBlank(morphId, "morph_id is required"),
                requireNonBlank(bloodlineId, "bloodline_id is required")
            );
            if (deleted == 0) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "bloodline not found");
            }
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "bloodline is referenced and cannot be deleted", e);
        }
    }

    private BloodlineMasterEntity normalizeForCreate(BloodlineMasterEntity request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "request body is required");
        }
        BloodlineMasterEntity normalized = new BloodlineMasterEntity();
        normalized.setSpeciesId(requireNonBlank(request.getSpeciesId(), "species_id is required"));
        normalized.setMorphId(requireNonBlank(request.getMorphId(), "morph_id is required"));
        normalized.setBloodlineName(requireNonBlank(request.getBloodlineName(), "bloodline_name is required"));
        normalized.setBloodlineId(generateNextBloodlineId(normalized.getSpeciesId(), normalized.getMorphId()));
        return normalized;
    }

    private BloodlineMasterEntity normalizeForUpdate(BloodlineMasterEntity request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "request body is required");
        }
        BloodlineMasterEntity normalized = new BloodlineMasterEntity();
        normalized.setSpeciesId(requireNonBlank(request.getSpeciesId(), "species_id is required"));
        normalized.setMorphId(requireNonBlank(request.getMorphId(), "morph_id is required"));
        normalized.setBloodlineId(requireNonBlank(request.getBloodlineId(), "bloodline_id is required"));
        normalized.setBloodlineName(requireNonBlank(request.getBloodlineName(), "bloodline_name is required"));
        return normalized;
    }

    private String generateNextBloodlineId(String speciesId, String morphId) {
        int max = 0;
        for (String existing : bloodlineMasterMapper.findBloodlineIdsBySpeciesAndMorph(speciesId, morphId)) {
            try {
                max = Math.max(max, Integer.parseInt(existing));
            } catch (NumberFormatException ignored) {
                // skip non-numeric ids
            }
        }
        return String.format("%03d", max + 1);
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
