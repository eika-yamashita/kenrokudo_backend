package com.backend.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.backend.entity.MorphComponentEntity;
import com.backend.entity.MorphMasterEntity;
import com.backend.mapper.MorphComponentMapper;
import com.backend.mapper.MorphMasterMapper;

@Service
public class MorphMasterService {

    private static final String MORPH_SINGLE = "SINGLE";
    private static final String MORPH_COMBO = "COMBO";
    private static final String EXPRESSION_VISUAL = "0";
    private static final Set<String> INHERITANCE_CATEGORIES = Set.of("0", "1", "2", "3");
    private static final String SYSTEM_USER = "system";

    private final MorphMasterMapper morphMasterMapper;
    private final MorphComponentMapper morphComponentMapper;

    public MorphMasterService(MorphMasterMapper morphMasterMapper, MorphComponentMapper morphComponentMapper) {
        this.morphMasterMapper = morphMasterMapper;
        this.morphComponentMapper = morphComponentMapper;
    }

    public List<MorphMasterEntity> getMorphs() {
        return attachComponents(morphMasterMapper.findAll());
    }

    public List<MorphMasterEntity> searchMorphs(String speciesIdRaw) {
        return attachComponents(morphMasterMapper.search(trimToNull(speciesIdRaw)));
    }

    public MorphMasterEntity getMorph(String speciesId, String morphId) {
        MorphMasterEntity morph = morphMasterMapper.findByPrimaryKey(
            requireNonBlank(speciesId, "species_id is required"),
            requireNonBlank(morphId, "morph_id is required")
        );
        if (morph == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "morph not found");
        }
        attachComponents(morph);
        return morph;
    }

    public MorphMasterEntity getMorphByName(String speciesId, String morphName) {
        if (trimToNull(speciesId) == null || trimToNull(morphName) == null) {
            return null;
        }
        MorphMasterEntity morph = morphMasterMapper.findBySpeciesIdAndMorphName(speciesId, morphName);
        if (morph != null) {
            attachComponents(morph);
        }
        return morph;
    }

    @Transactional
    public MorphMasterEntity createMorph(MorphMasterEntity request) {
        MorphMasterEntity normalized = normalize(request, null, null);
        normalized.setMorphId(generateNextMorphId(normalized.getSpeciesId()));
        normalized.setCreateUser(SYSTEM_USER);
        normalized.setUpdateUser(SYSTEM_USER);
        try {
            morphMasterMapper.insert(normalized);
            saveComponents(normalized);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "morph already exists", e);
        }
        return getMorph(normalized.getSpeciesId(), normalized.getMorphId());
    }

    @Transactional
    public MorphMasterEntity updateMorph(String speciesId, String morphId, MorphMasterEntity request) {
        getMorph(speciesId, morphId);
        MorphMasterEntity normalized = normalize(request, speciesId, morphId);
        normalized.setUpdateUser(SYSTEM_USER);
        try {
            if (morphMasterMapper.update(speciesId, morphId, normalized) == 0) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "morph not found");
            }
            morphComponentMapper.deleteByCombo(speciesId, morphId);
            saveComponents(normalized);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "morph update conflicts with existing data", e);
        }
        return getMorph(speciesId, morphId);
    }

    @Transactional
    public void deleteMorph(String speciesId, String morphId) {
        try {
            if (morphMasterMapper.delete(
                requireNonBlank(speciesId, "species_id is required"),
                requireNonBlank(morphId, "morph_id is required")
            ) == 0) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "morph not found");
            }
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "morph is referenced and cannot be deleted", e);
        }
    }

    private MorphMasterEntity normalize(MorphMasterEntity request, String pathSpeciesId, String pathMorphId) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "request body is required");
        }
        MorphMasterEntity normalized = new MorphMasterEntity();
        normalized.setSpeciesId(pathSpeciesId != null
            ? requireNonBlank(pathSpeciesId, "species_id is required")
            : requireNonBlank(request.getSpeciesId(), "species_id is required"));
        normalized.setMorphId(pathMorphId);
        normalized.setMorphName(requireNonBlank(request.getMorphName(), "morph_name is required"));

        String morphType = requireNonBlank(request.getMorphType(), "morph_type is required").toUpperCase();
        if (!MORPH_SINGLE.equals(morphType) && !MORPH_COMBO.equals(morphType)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "morph_type must be SINGLE or COMBO");
        }
        normalized.setMorphType(morphType);
        normalized.setDisplayPriority(request.getDisplayPriority() == null ? 0 : request.getDisplayPriority());

        if (MORPH_SINGLE.equals(morphType)) {
            String inheritanceCategory = requireNonBlank(request.getInheritanceCategory(), "inheritance_category is required");
            if (!INHERITANCE_CATEGORIES.contains(inheritanceCategory)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid inheritance_category");
            }
            normalized.setInheritanceCategory(inheritanceCategory);
            normalized.setComponents(List.of());
        } else {
            normalized.setInheritanceCategory(null);
            normalized.setComponents(validateComponents(normalized.getSpeciesId(), request.getComponents()));
        }
        return normalized;
    }

    private List<MorphComponentEntity> validateComponents(String speciesId, List<MorphComponentEntity> requested) {
        if (requested == null || requested.size() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "combo morph requires at least two components");
        }
        List<MorphComponentEntity> components = new ArrayList<>();
        Set<String> usedIds = new HashSet<>();
        for (int index = 0; index < requested.size(); index++) {
            String componentMorphId = requireNonBlank(requested.get(index).getComponentMorphId(), "component_morph_id is required");
            if (!usedIds.add(componentMorphId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "combo component cannot be duplicated");
            }
            MorphMasterEntity componentMorph = morphMasterMapper.findByPrimaryKey(speciesId, componentMorphId);
            if (componentMorph == null || !MORPH_SINGLE.equals(componentMorph.getMorphType())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "combo component must be a SINGLE morph in the same species");
            }
            MorphComponentEntity component = new MorphComponentEntity();
            component.setSpeciesId(speciesId);
            component.setComponentMorphId(componentMorphId);
            component.setRequiredExpression(EXPRESSION_VISUAL);
            component.setSortOrder(index);
            components.add(component);
        }
        return components;
    }

    private void saveComponents(MorphMasterEntity morph) {
        if (!MORPH_COMBO.equals(morph.getMorphType()) || morph.getComponents() == null) {
            return;
        }
        for (MorphComponentEntity component : morph.getComponents()) {
            component.setSpeciesId(morph.getSpeciesId());
            component.setComboMorphId(morph.getMorphId());
            morphComponentMapper.insert(component);
        }
    }

    private List<MorphMasterEntity> attachComponents(List<MorphMasterEntity> morphs) {
        for (MorphMasterEntity morph : morphs) {
            attachComponents(morph);
        }
        return morphs;
    }

    private void attachComponents(MorphMasterEntity morph) {
        morph.setComponents(MORPH_COMBO.equals(morph.getMorphType())
            ? morphComponentMapper.findByCombo(morph.getSpeciesId(), morph.getMorphId())
            : List.of());
    }

    private String generateNextMorphId(String speciesId) {
        int max = 0;
        for (String existing : morphMasterMapper.findMorphIdsBySpeciesId(speciesId)) {
            try {
                max = Math.max(max, Integer.parseInt(existing));
            } catch (NumberFormatException ignored) {
                // Ignore non-numeric IDs when generating the next numeric ID.
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
