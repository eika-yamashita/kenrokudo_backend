package com.backend.service;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.backend.entity.IndividualEntity;
import com.backend.entity.PairingEntity;
import com.backend.mapper.IndividualMapper;
import com.backend.mapper.PairingMapper;

@Service
public class PairingService {

    private static final Pattern PAIRING_ID_PATTERN = Pattern.compile("^[A-Z]+$");

    private final PairingMapper pairingMapper;
    private final IndividualMapper individualMapper;

    public PairingService(PairingMapper pairingMapper, IndividualMapper individualMapper) {
        this.pairingMapper = pairingMapper;
        this.individualMapper = individualMapper;
    }

    public List<PairingEntity> getPairings() {
        return pairingMapper.findAll();
    }

    public List<PairingEntity> searchPairings(String speciesIdRaw, Integer fiscalYear) {
        if (fiscalYear != null && fiscalYear < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fiscal_year must be zero or positive");
        }

        String speciesId = trimToNull(speciesIdRaw);
        return pairingMapper.search(speciesId, fiscalYear);
    }

    public PairingEntity getPairing(String speciesId, Integer fiscalYear, String pairingId) {
        PairingEntity pairing = pairingMapper.findByPrimaryKey(speciesId, fiscalYear, pairingId);
        if (pairing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "pairing not found");
        }
        return pairing;
    }

    @Transactional
    public PairingEntity createPairing(PairingEntity request) {
        PairingEntity normalized = normalizeForCreate(request);

        ensureParentValid(normalized.getSpeciesId(), normalized.getMaleParentId(), true);
        ensureParentValid(normalized.getSpeciesId(), normalized.getFemaleParentId(), false);
        ensureNoDuplicatePair(normalized);

        try {
            pairingMapper.insert(normalized);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "pairing already exists", e);
        }

        return getPairing(normalized.getSpeciesId(), normalized.getFiscalYear(), normalized.getPairingId());
    }

    @Transactional
    public PairingEntity updatePairing(String speciesId, Integer fiscalYear, String pairingId, PairingEntity request) {
        PairingEntity existing = pairingMapper.findByPrimaryKey(speciesId, fiscalYear, pairingId);
        if (existing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "pairing not found");
        }

        PairingEntity normalized = normalizeForUpdate(existing, request);

        ensureParentValid(normalized.getSpeciesId(), normalized.getMaleParentId(), true);
        ensureParentValid(normalized.getSpeciesId(), normalized.getFemaleParentId(), false);
        ensureNoDuplicatePairExcluding(normalized, speciesId, fiscalYear, pairingId);

        try {
            int updated = pairingMapper.update(speciesId, fiscalYear, pairingId, normalized);
            if (updated == 0) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "pairing not found");
            }
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "pairing update conflicts with existing data", e);
        }

        return getPairing(normalized.getSpeciesId(), normalized.getFiscalYear(), normalized.getPairingId());
    }

    @Transactional
    public void deletePairing(String speciesId, Integer fiscalYear, String pairingId) {
        int deleted = pairingMapper.delete(speciesId, fiscalYear, pairingId);
        if (deleted == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "pairing not found");
        }
    }

    private PairingEntity normalizeForCreate(PairingEntity request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "request body is required");
        }

        PairingEntity normalized = new PairingEntity();
        normalized.setSpeciesId(requireNonBlank(request.getSpeciesId(), "species_id is required"));
        normalized.setPairingDate(requireDate(request.getPairingDate(), "pairing_date is required"));
        normalized.setFiscalYear(calculateFiscalYear(normalized.getPairingDate()));

        String manualPairingId = trimToNull(request.getPairingId());
        if (manualPairingId == null) {
            normalized.setPairingId(generateNextPairingId(normalized.getSpeciesId(), normalized.getFiscalYear()));
        } else {
            validatePairingId(manualPairingId);
            normalized.setPairingId(manualPairingId);
        }

        normalized.setMaleParentId(requireNonBlank(request.getMaleParentId(), "male_parent_id is required"));
        normalized.setFemaleParentId(requireNonBlank(request.getFemaleParentId(), "female_parent_id is required"));
        normalized.setNote(trimToNull(request.getNote()));

        return normalized;
    }

    private PairingEntity normalizeForUpdate(PairingEntity existing, PairingEntity request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "request body is required");
        }

        PairingEntity normalized = new PairingEntity();

        String speciesId = trimToNull(request.getSpeciesId());
        normalized.setSpeciesId(speciesId != null ? speciesId : existing.getSpeciesId());

        Date pairingDate = request.getPairingDate() != null ? request.getPairingDate() : existing.getPairingDate();
        normalized.setPairingDate(requireDate(pairingDate, "pairing_date is required"));
        normalized.setFiscalYear(calculateFiscalYear(normalized.getPairingDate()));

        String pairingId = trimToNull(request.getPairingId());
        pairingId = pairingId != null ? pairingId : existing.getPairingId();
        validatePairingId(pairingId);
        normalized.setPairingId(pairingId);

        String maleParentId = trimToNull(request.getMaleParentId());
        maleParentId = maleParentId != null ? maleParentId : existing.getMaleParentId();
        normalized.setMaleParentId(requireNonBlank(maleParentId, "male_parent_id is required"));

        String femaleParentId = trimToNull(request.getFemaleParentId());
        femaleParentId = femaleParentId != null ? femaleParentId : existing.getFemaleParentId();
        normalized.setFemaleParentId(requireNonBlank(femaleParentId, "female_parent_id is required"));

        normalized.setNote(trimToNull(request.getNote()));
        return normalized;
    }

    private void ensureParentValid(String speciesId, String parentId, boolean maleParent) {
        IndividualEntity parent = individualMapper.findBySpeciesIdAndId(speciesId, parentId);
        if (parent == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "parent individual not found");
        }

        String genderCategory = trimToNull(parent.getGenderCategory());
        if (maleParent && !isMaleGender(genderCategory)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "male_parent_id must be male individual");
        }
        if (!maleParent && !isFemaleGender(genderCategory)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "female_parent_id must be female individual");
        }
    }

    private void ensureNoDuplicatePair(PairingEntity pairing) {
        int count = pairingMapper.countByPairingSignature(
            pairing.getSpeciesId(),
            pairing.getMaleParentId(),
            pairing.getFemaleParentId(),
            pairing.getPairingDate()
        );
        if (count > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "pairing already exists for same species/parents/date");
        }
    }

    private void ensureNoDuplicatePairExcluding(PairingEntity pairing, String speciesId, Integer fiscalYear, String pairingId) {
        int count = pairingMapper.countByPairingSignatureExcludingPrimaryKey(
            pairing.getSpeciesId(),
            pairing.getMaleParentId(),
            pairing.getFemaleParentId(),
            pairing.getPairingDate(),
            speciesId,
            fiscalYear,
            pairingId
        );
        if (count > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "pairing already exists for same species/parents/date");
        }
    }

    private String generateNextPairingId(String speciesId, Integer fiscalYear) {
        List<String> existingIds = pairingMapper.findPairingIdsBySpeciesAndYear(speciesId, fiscalYear);

        int maxOrder = 0;
        for (String id : existingIds) {
            if (id == null || !PAIRING_ID_PATTERN.matcher(id).matches()) {
                continue;
            }
            int order = decodePairingId(id);
            if (order > maxOrder) {
                maxOrder = order;
            }
        }

        return encodePairingId(maxOrder + 1);
    }

    private void validatePairingId(String pairingId) {
        if (pairingId == null || !PAIRING_ID_PATTERN.matcher(pairingId).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "pairing_id must be uppercase letters (A-Z, AA, AB...)");
        }
    }

    private int calculateFiscalYear(Date pairingDate) {
        return pairingDate.toInstant().atZone(ZoneId.systemDefault()).getYear();
    }

    private int decodePairingId(String pairingId) {
        int value = 0;
        for (int i = 0; i < pairingId.length(); i++) {
            int digit = pairingId.charAt(i) - 'A' + 1;
            value = value * 26 + digit;
        }
        return value;
    }

    private String encodePairingId(int order) {
        if (order <= 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "pairing_id sequence is invalid");
        }

        StringBuilder sb = new StringBuilder();
        int n = order;
        while (n > 0) {
            int remainder = (n - 1) % 26;
            sb.append((char) ('A' + remainder));
            n = (n - 1) / 26;
        }
        return sb.reverse().toString();
    }

    private String requireNonBlank(String value, String message) {
        String normalized = trimToNull(value);
        if (normalized == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
        return normalized;
    }

    private Date requireDate(Date value, String message) {
        if (value == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
        return value;
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private boolean isMaleGender(String gender) {
        return "1".equals(gender) || "M".equalsIgnoreCase(gender) || "♂".equals(gender);
    }

    private boolean isFemaleGender(String gender) {
        return "2".equals(gender) || "F".equalsIgnoreCase(gender) || "♀".equals(gender);
    }
}
