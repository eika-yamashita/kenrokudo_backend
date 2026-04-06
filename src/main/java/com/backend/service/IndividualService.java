package com.backend.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.time.ZoneId;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.backend.entity.IndividualEntity;
import com.backend.entity.PairingEntity;
import com.backend.mapper.IndividualMapper;
import com.backend.mapper.PairingMapper;
import com.backend.model.Individual;

@Service
public class IndividualService {

    private static final String SEQ_CHARS = "0123456789ABCDEFGHJKMNPQRSTUVWXYZ";
    private static final int ID_LENGTH = 6;
    private static final int SEQ_WIDTH = 3;
    private static final int MAX_SEQUENCE = (int) Math.pow(SEQ_CHARS.length(), SEQ_WIDTH) - 1;
    private static final int MAX_INSERT_RETRY = 5;
    private static final Map<Character, Integer> SEQ_INDEX_MAP = createSeqIndexMap();

    private final IndividualMapper individualMapper;
    private final PairingMapper pairingMapper;

    public IndividualService(IndividualMapper individualMapper, PairingMapper pairingMapper) {
        this.individualMapper = individualMapper;
        this.pairingMapper = pairingMapper;
    }

    public List<Individual> getIndividuals() {
        List<IndividualEntity> entityList = individualMapper.findAll();
        List<Individual> individualList = new ArrayList<>();

        for (IndividualEntity entity : entityList) {
            Individual individual = new Individual();
            individual = setIndividual(individual, entity);
            individualList.add(individual);
        }

        return individualList;
    }

    public IndividualEntity getIndividual(String speciesId, String id) {
        return individualMapper.findBySpeciesIdAndId(speciesId, id);
    }

    public Individual getIndividualModel(String speciesId, String id) {
        IndividualEntity entity = getIndividual(speciesId, id);
        if (entity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "individual not found");
        }

        Individual model = new Individual();
        return setIndividual(model, entity);
    }

    @Transactional
    public Individual createIndividual(Individual individual) {
        String speciesId = trimToNull(individual.getSpeciesId());
        if (speciesId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "species_id is required");
        }
        individual.setSpeciesId(speciesId);
        applyPairingParentsIfSpecified(individual, speciesId);

        String id = trimToNull(individual.getId());
        boolean autoIdRequired = id == null;
        if (!autoIdRequired) {
            individual.setId(id);
        }

        if (autoIdRequired) {
            id = generateNextId(individual.getCreateAt(), individual.getBreedingCategory());
            individual.setId(id);
        }

        if (!autoIdRequired && individualMapper.findBySpeciesIdAndId(speciesId, id) != null) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                String.format("individual already exists (species_id=%s, id=%s)", speciesId, id)
            );
        }

        for (int attempt = 0; attempt < MAX_INSERT_RETRY; attempt++) {
            try {
                individualMapper.insert(individual);
                break;
            } catch (DataIntegrityViolationException e) {
                if (!autoIdRequired || attempt == MAX_INSERT_RETRY - 1) {
                    throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        String.format("individual already exists (species_id=%s, id=%s)", speciesId, individual.getId()),
                        e
                    );
                }
                individual.setId(generateNextId(individual.getCreateAt(), individual.getBreedingCategory()));
            }
        }

        IndividualEntity entity = individualMapper.findBySpeciesIdAndId(individual.getSpeciesId(), individual.getId());
        Individual model = new Individual();
        model = setIndividual(model, entity);
        return model;
    }

    public void updateIndividual(String speciesId, String id, Individual individual) {
        if (getIndividual(speciesId, id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "individual not found");
        }

        int updated = individualMapper.update(speciesId, id, individual);
        if (updated == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "individual not found");
        }
    }

    public void deleteIndividual(String speciesId, String id) {
        int deleted = individualMapper.delete(speciesId, id);
        if (deleted == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "individual not found");
        }
    }

    private Individual setIndividual(Individual model, IndividualEntity entity) {
        model.setSpeciesId(entity.getSpeciesId());
        model.setId(entity.getId());
        model.setMaleParentId(entity.getMaleParentId());
        model.setFemaleParentId(entity.getFemaleParentId());
        model.setMorph(entity.getMorph());
        model.setBloodline(entity.getBloodline());
        model.setGenderCategory(entity.getGenderCategory());
        model.setBreedingCategory(entity.getBreedingCategory());
        model.setBreeder(entity.getBreeder());
        model.setClutchDate(entity.getClutchDate());
        model.setHatchDate(entity.getHatchDate());
        model.setPurchaseFrom(entity.getPurchaseFrom());
        model.setPurchasePrice(entity.getPurchasePrice());
        model.setPurchaseDate(entity.getPurchaseDate());
        model.setSalesCategory(entity.getSalesCategory());
        model.setSalesTo(entity.getSalesTo());
        model.setSalesPriceTaxEx(entity.getSalesPriceTaxEx());
        model.setSalesPriceTax(entity.getSalesPriceTax());
        model.setSalesPriceTaxIn(entity.getSalesPriceTaxIn());
        model.setSalesDate(entity.getSalesDate());
        model.setDeathDate(entity.getDeathDate());
        model.setNote(entity.getNote());
        model.setCreateUser(entity.getCreateUser());
        model.setCreateAt(entity.getCreateAt());
        model.setUpdateUser(entity.getUpdateUser());
        model.setUpdateAt(entity.getUpdateAt());
        return model;
    }

    private String generateNextId(Date createAt, String breedingCategoryRaw) {
        Date baseDate = Objects.requireNonNullElseGet(createAt, Date::new);
        int year = baseDate.toInstant().atZone(ZoneId.systemDefault()).getYear();
        String yy = String.format("%02d", year % 100);
        String breedingMarker = resolveBreedingMarker(breedingCategoryRaw);
        String prefix = yy + breedingMarker;

        int maxSequence = -1;
        for (String existingId : individualMapper.findIdsByIdPrefix(prefix)) {
            int sequence = parseSequence(existingId, prefix);
            if (sequence > maxSequence) {
                maxSequence = sequence;
            }
        }

        int nextSequence = Math.max(maxSequence + 1, 1);
        if (nextSequence > MAX_SEQUENCE) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                String.format("auto id exhausted for prefix=%s", prefix)
            );
        }

        return prefix + encodeSequence(nextSequence);
    }

    private String resolveBreedingMarker(String rawCategory) {
        String category = trimToNull(rawCategory);
        if (category == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "breeding_category is required for auto id generation");
        }

        if ("0".equals(category) || "A".equalsIgnoreCase(category)) {
            return "A";
        }
        if ("1".equals(category) || "B".equalsIgnoreCase(category)) {
            return "B";
        }

        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "breeding_category must be 0/1 or A/B for auto id generation"
        );
    }

    private int parseSequence(String id, String prefix) {
        if (id == null || id.length() != ID_LENGTH || !id.startsWith(prefix)) {
            return -1;
        }

        int value = 0;
        for (int i = prefix.length(); i < id.length(); i++) {
            char c = id.charAt(i);
            Integer index = SEQ_INDEX_MAP.get(c);
            if (index == null) {
                return -1;
            }
            value = value * SEQ_CHARS.length() + index;
        }
        return value;
    }

    private String encodeSequence(int value) {
        int radix = SEQ_CHARS.length();
        char[] result = new char[SEQ_WIDTH];
        int current = value;
        for (int i = SEQ_WIDTH - 1; i >= 0; i--) {
            result[i] = SEQ_CHARS.charAt(current % radix);
            current /= radix;
        }
        return new String(result);
    }

    private void applyPairingParentsIfSpecified(Individual individual, String speciesId) {
        String pairingId = trimToNull(individual.getPairingId());
        Integer pairingFiscalYear = individual.getPairingFiscalYear();

        if (pairingId == null && pairingFiscalYear == null) {
            return;
        }

        if (pairingId == null || pairingFiscalYear == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "pairing_id and pairing_fiscal_year must be provided together"
            );
        }

        PairingEntity pairing = pairingMapper.findByPrimaryKey(speciesId, pairingFiscalYear, pairingId);
        if (pairing == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                String.format(
                    "pairing not found (species_id=%s, fiscal_year=%s, pairing_id=%s)",
                    speciesId,
                    pairingFiscalYear,
                    pairingId
                )
            );
        }

        individual.setPairingId(pairingId);
        individual.setPairingFiscalYear(pairingFiscalYear);
        individual.setMaleParentId(pairing.getMaleParentId());
        individual.setFemaleParentId(pairing.getFemaleParentId());
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private static Map<Character, Integer> createSeqIndexMap() {
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < SEQ_CHARS.length(); i++) {
            map.put(SEQ_CHARS.charAt(i), i);
        }
        return map;
    }
}
