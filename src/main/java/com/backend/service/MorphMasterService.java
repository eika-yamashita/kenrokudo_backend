package com.backend.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.backend.entity.MorphMasterEntity;
import com.backend.mapper.MorphMasterMapper;

@Service
public class MorphMasterService {

    private static final String POLYGENIC = "0";
    private static final String RECESSIVE = "1";
    private static final String DOMINANT = "2";
    private static final String CODOMINANT = "3";
    private static final String SYSTEM_USER = "system";

    private final MorphMasterMapper morphMasterMapper;

    public MorphMasterService(MorphMasterMapper morphMasterMapper) {
        this.morphMasterMapper = morphMasterMapper;
    }

    public List<MorphMasterEntity> getMorphs() {
        return morphMasterMapper.findAll();
    }

    public List<MorphMasterEntity> searchMorphs(String speciesIdRaw) {
        return morphMasterMapper.search(trimToNull(speciesIdRaw));
    }

    public MorphMasterEntity getMorph(String speciesId, String morphId) {
        MorphMasterEntity morph = morphMasterMapper.findByPrimaryKey(
            requireNonBlank(speciesId, "種は必須です"),
            requireNonBlank(morphId, "モルフIDは必須です")
        );
        if (morph == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "モルフが見つかりません");
        }
        return morph;
    }

    public MorphMasterEntity getMorphByName(String speciesId, String morphName) {
        if (trimToNull(speciesId) == null || trimToNull(morphName) == null) {
            return null;
        }
        return morphMasterMapper.findBySpeciesIdAndMorphName(speciesId, morphName);
    }

    @Transactional
    public MorphMasterEntity createMorph(MorphMasterEntity request) {
        MorphMasterEntity normalized = normalizeForCreate(request);
        try {
            morphMasterMapper.insert(normalized);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "同じモルフが既に登録されています", e);
        }
        return getMorph(normalized.getSpeciesId(), normalized.getMorphId());
    }

    @Transactional
    public MorphMasterEntity updateMorph(String speciesId, String morphId, MorphMasterEntity request) {
        getMorph(speciesId, morphId);
        MorphMasterEntity normalized = normalizeForUpdate(speciesId, morphId, request);
        try {
            int updated = morphMasterMapper.update(speciesId, morphId, normalized);
            if (updated == 0) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "モルフが見つかりません");
            }
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "同じモルフ名が既に登録されているため更新できません", e);
        }
        return getMorph(normalized.getSpeciesId(), normalized.getMorphId());
    }

    @Transactional
    public void deleteMorph(String speciesId, String morphId) {
        try {
            int deleted = morphMasterMapper.delete(
                requireNonBlank(speciesId, "species_id is required"),
                requireNonBlank(morphId, "morph_id is required")
            );
            if (deleted == 0) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "モルフが見つかりません");
            }
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "このモルフは血統または個体情報で使用されているため削除できません", e);
        }
    }

    private MorphMasterEntity normalizeForCreate(MorphMasterEntity request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "リクエスト内容が指定されていません");
        }
        MorphMasterEntity normalized = new MorphMasterEntity();
        normalized.setSpeciesId(requireNonBlank(request.getSpeciesId(), "種は必須です"));
        normalized.setMorphName(requireNonBlank(request.getMorphName(), "モルフ名は必須です"));
        normalized.setInheritanceCategory(validateInheritanceCategory(request.getInheritanceCategory()));
        normalized.setMorphId(generateNextMorphId(normalized.getSpeciesId()));
        normalized.setCreateUser(SYSTEM_USER);
        normalized.setUpdateUser(SYSTEM_USER);
        return normalized;
    }

    private MorphMasterEntity normalizeForUpdate(String speciesId, String morphId, MorphMasterEntity request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "リクエスト内容が指定されていません");
        }
        MorphMasterEntity normalized = new MorphMasterEntity();
        normalized.setSpeciesId(requireNonBlank(request.getSpeciesId(), "種は必須です"));
        normalized.setMorphId(requireNonBlank(request.getMorphId(), "モルフIDは必須です"));
        normalized.setMorphName(requireNonBlank(request.getMorphName(), "モルフ名は必須です"));
        normalized.setInheritanceCategory(validateInheritanceCategory(request.getInheritanceCategory()));
        normalized.setUpdateUser(SYSTEM_USER);
        return normalized;
    }

    private String generateNextMorphId(String speciesId) {
        int max = 0;
        for (String existing : morphMasterMapper.findMorphIdsBySpeciesId(speciesId)) {
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

    private String validateInheritanceCategory(String rawCategory) {
        String category = requireNonBlank(rawCategory, "遺伝性区分は必須です");
        if (POLYGENIC.equals(category) || RECESSIVE.equals(category) || DOMINANT.equals(category) || CODOMINANT.equals(category)) {
            return category;
        }
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "遺伝性区分は 0, 1, 2, 3 のいずれかを指定してください"
        );
    }
}
