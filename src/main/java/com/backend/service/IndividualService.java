package com.backend.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.backend.entity.BloodlineMasterEntity;
import com.backend.entity.IndividualEntity;
import com.backend.entity.IndividualBloodlineEntity;
import com.backend.entity.IndividualMorphEntity;
import com.backend.entity.MorphMasterEntity;
import com.backend.entity.PairingEntity;
import com.backend.mapper.BloodlineMasterMapper;
import com.backend.mapper.IndividualMapper;
import com.backend.mapper.IndividualBloodlineMapper;
import com.backend.mapper.IndividualMorphMapper;
import com.backend.mapper.MorphMasterMapper;
import com.backend.mapper.PairingMapper;
import com.backend.model.Individual;
import com.backend.model.IndividualMorph;

@Service
public class IndividualService {

    private static final String SEQ_CHARS = "0123456789ABCDEFGHJKMNPQRSTUVWXYZ";
    private static final int ID_LENGTH = 6;
    private static final int SEQ_WIDTH = 3;
    private static final int MAX_SEQUENCE = (int) Math.pow(SEQ_CHARS.length(), SEQ_WIDTH) - 1;
    private static final int MAX_INSERT_RETRY = 5;
    private static final String EXPRESSION_VISUAL = "0";
    private static final String EXPRESSION_HET = "1";
    private static final String EXPRESSION_POSSIBLE_HET = "2";
    private static final String INHERITANCE_RECESSIVE = "1";
    private static final String MORPH_SINGLE = "SINGLE";
    private static final Map<Character, Integer> SEQ_INDEX_MAP = createSeqIndexMap();

    private final IndividualMapper individualMapper;
    private final PairingMapper pairingMapper;
    private final MorphMasterMapper morphMasterMapper;
    private final BloodlineMasterMapper bloodlineMasterMapper;
    private final IndividualMorphMapper individualMorphMapper;
    private final IndividualBloodlineMapper individualBloodlineMapper;
    private final IndividualImageService individualImageService;

    public IndividualService(
        IndividualMapper individualMapper,
        PairingMapper pairingMapper,
        MorphMasterMapper morphMasterMapper,
        BloodlineMasterMapper bloodlineMasterMapper,
        IndividualMorphMapper individualMorphMapper,
        IndividualBloodlineMapper individualBloodlineMapper,
        IndividualImageService individualImageService
    ) {
        this.individualMapper = individualMapper;
        this.pairingMapper = pairingMapper;
        this.morphMasterMapper = morphMasterMapper;
        this.bloodlineMasterMapper = bloodlineMasterMapper;
        this.individualMorphMapper = individualMorphMapper;
        this.individualBloodlineMapper = individualBloodlineMapper;
        this.individualImageService = individualImageService;
    }

    public List<Individual> getIndividuals() {
        List<IndividualEntity> entityList = individualMapper.findAll();
        List<Individual> individualList = new ArrayList<>();

        for (IndividualEntity entity : entityList) {
            individualList.add(setIndividual(new Individual(), entity));
        }

        return individualList;
    }

    public List<Individual> searchIndividuals(String speciesIdRaw, Integer fiscalYear, String morphRaw) {
        if (fiscalYear != null && fiscalYear < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fiscal_year must be zero or positive");
        }

        String speciesId = trimToNull(speciesIdRaw);
        String morph = trimToNull(morphRaw);

        List<IndividualEntity> entityList = individualMapper.search(speciesId, fiscalYear, morph);
        List<Individual> individualList = new ArrayList<>();
        for (IndividualEntity entity : entityList) {
            individualList.add(setIndividual(new Individual(), entity));
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

        return setIndividual(new Individual(), entity);
    }

    @Transactional
    public Individual createIndividual(Individual individual) {
        String speciesId = trimToNull(individual.getSpeciesId());
        if (speciesId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "species_id is required");
        }

        Integer fiscalYear = individual.getFiscalYear();
        if (fiscalYear == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fiscal_year is required");
        }

        individual.setSpeciesId(speciesId);
        Date registeredAt = new Date();
        individual.setCreateUser("system");
        individual.setCreateAt(registeredAt);
        individual.setUpdateUser("system");
        individual.setUpdateAt(registeredAt);
        applyPairingParentsIfSpecified(individual, speciesId);
        applyMorphAndBloodlineIfSpecified(individual, speciesId);

        String id = trimToNull(individual.getId());
        boolean autoIdRequired = id == null;
        if (!autoIdRequired) {
            individual.setId(id);
        }

        if (autoIdRequired) {
            id = generateNextId(speciesId, individual.getFiscalYear(), individual.getBreedingCategory());
            individual.setId(id);
        }

        if (!autoIdRequired && individualMapper.findBySpeciesIdAndId(speciesId, id) != null) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                String.format("individual already exists (species_id=%s, id=%s)", speciesId, id)
            );
        }

        List<IndividualMorphEntity> morphEntries = normalizeMorphEntries(individual, speciesId, individual.getId());

        for (int attempt = 0; attempt < MAX_INSERT_RETRY; attempt++) {
            try {
                individualMapper.insert(individual);
                saveIndividualMorphs(speciesId, individual.getId(), morphEntries, individual);
                saveIndividualBloodline(speciesId, individual.getId(), morphEntries, individual);
                break;
            } catch (DataIntegrityViolationException e) {
                if (!autoIdRequired || attempt == MAX_INSERT_RETRY - 1) {
                    throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        String.format("individual already exists (species_id=%s, id=%s)", speciesId, individual.getId()),
                        e
                    );
                }
                individual.setId(generateNextId(speciesId, individual.getFiscalYear(), individual.getBreedingCategory()));
                morphEntries = normalizeMorphEntries(individual, speciesId, individual.getId());
            }
        }

        IndividualEntity entity = individualMapper.findBySpeciesIdAndId(individual.getSpeciesId(), individual.getId());
        return setIndividual(new Individual(), entity);
    }

    @Transactional
    public void updateIndividual(String speciesId, String id, Individual individual) {
        if (getIndividual(speciesId, id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "individual not found");
        }

        individual.setSpeciesId(speciesId);
        individual.setUpdateUser("system");
        individual.setUpdateAt(new Date());
        if (individual.getFiscalYear() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fiscal_year is required");
        }
        applyPairingParentsIfSpecified(individual, speciesId);
        applyMorphAndBloodlineIfSpecified(individual, speciesId);
        List<IndividualMorphEntity> morphEntries = normalizeMorphEntries(individual, speciesId, id);

        int updated = individualMapper.update(speciesId, id, individual);
        if (updated == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "individual not found");
        }

        individualBloodlineMapper.deleteByIndividual(speciesId, id);
        individualMorphMapper.deleteByIndividual(speciesId, id);
        saveIndividualMorphs(speciesId, id, morphEntries, individual);
        saveIndividualBloodline(speciesId, id, morphEntries, individual);
    }

    @Transactional
    public void deleteIndividual(String speciesId, String id) {
        if (getIndividual(speciesId, id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "individual not found");
        }

        individualImageService.deleteImagesByIndividual(speciesId, id);
        int deleted = individualMapper.delete(speciesId, id);
        if (deleted == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "individual not found");
        }
    }

    private Individual setIndividual(Individual model, IndividualEntity entity) {
        model.setSpeciesId(entity.getSpeciesId());
        model.setFiscalYear(entity.getFiscalYear());
        model.setId(entity.getId());
        model.setPairingFiscalYear(entity.getPairingFiscalYear());
        model.setPairingId(entity.getPairingId());
        model.setMaleParentId(entity.getMaleParentId());
        model.setFemaleParentId(entity.getFemaleParentId());
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

        List<IndividualMorphEntity> morphEntities = individualMorphMapper.findByIndividual(entity.getSpeciesId(), entity.getId());
        List<IndividualMorph> morphEntries = toIndividualMorphModels(morphEntities);
        model.setMorphEntries(morphEntries);
        return model;
    }

    private List<IndividualMorph> toIndividualMorphModels(List<IndividualMorphEntity> entities) {
        List<IndividualMorph> models = new ArrayList<>();
        for (IndividualMorphEntity entity : entities) {
            IndividualMorph model = new IndividualMorph();
            model.setMorphId(entity.getMorphId());
            model.setMorphName(entity.getMorphName());
            model.setBloodlineId(entity.getBloodlineId());
            model.setBloodlineName(entity.getBloodlineName());
            model.setExpressionCategory(entity.getExpressionCategory());
            model.setPossibleHetPercentage(entity.getPossibleHetPercentage());
            model.setSortOrder(entity.getSortOrder());
            models.add(model);
        }
        return models;
    }

    private String generateNextId(String speciesId, Integer fiscalYear, String breedingCategoryRaw) {
        if (fiscalYear == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fiscal_year is required for auto id generation");
        }
        String yy = String.format("%02d", Math.floorMod(fiscalYear, 100));
        String breedingMarker = resolveBreedingMarker(breedingCategoryRaw);
        String prefix = yy + breedingMarker;

        int maxSequence = -1;
        for (String existingId : individualMapper.findIdsBySpeciesIdAndIdPrefix(speciesId, prefix)) {
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

    private void applyMorphAndBloodlineIfSpecified(Individual individual, String speciesId) {
        IndividualMorph visualEntry = resolveVisualMorphEntry(individual);
        if (visualEntry == null) {
            return;
        }

        String visualMorphId = trimToNull(visualEntry.getMorphId());
        MorphMasterEntity visualMorph = morphMasterMapper.findByPrimaryKey(speciesId, visualMorphId);
        if (visualMorph == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "visual morph not found");
        }

        applyBloodlineToVisualEntry(individual, speciesId, visualEntry, visualMorphId);
    }

    private void applyBloodlineToVisualEntry(
        Individual individual,
        String speciesId,
        IndividualMorph visualEntry,
        String visualMorphId
    ) {
        String bloodlineId = trimToNull(visualEntry.getBloodlineId());
        String bloodlineName = trimToNull(visualEntry.getBloodlineName());

        if (bloodlineId == null && bloodlineName != null) {
            BloodlineMasterEntity bloodlineByName = bloodlineMasterMapper.findBySpeciesMorphAndBloodlineName(
                speciesId,
                visualMorphId,
                bloodlineName
            );
            bloodlineId = bloodlineByName != null ? bloodlineByName.getBloodlineId() : null;
        }

        if (bloodlineId == null) {
            visualEntry.setBloodlineId(null);
            visualEntry.setBloodlineName(null);
            return;
        }

        BloodlineMasterEntity bloodline = bloodlineMasterMapper.findByPrimaryKey(speciesId, visualMorphId, bloodlineId);
        if (bloodline == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "bloodline not found");
        }

        visualEntry.setBloodlineId(bloodlineId);
        visualEntry.setBloodlineName(bloodline.getBloodlineName());
    }

    private IndividualMorph resolveVisualMorphEntry(Individual individual) {
        if (individual.getMorphEntries() != null) {
            for (IndividualMorph entry : individual.getMorphEntries()) {
                if (EXPRESSION_VISUAL.equals(trimToNull(entry.getExpressionCategory()))) {
                    return entry;
                }
            }
        }

        return null;
    }

    private List<IndividualMorphEntity> normalizeMorphEntries(Individual individual, String speciesId, String individualId) {
        List<IndividualMorph> requestEntries = new ArrayList<>();
        if (individual.getMorphEntries() != null) {
            requestEntries.addAll(individual.getMorphEntries());
        }

        List<NormalizedMorphEntry> normalizedEntries = new ArrayList<>();
        int visualCount = 0;
        int hetOrder = 0;
        int possibleOrder = 0;
        Set<String> usedMorphIds = new LinkedHashSet<>();

        for (IndividualMorph requestEntry : requestEntries) {
            String expressionCategory = trimToNull(requestEntry.getExpressionCategory());
            String morphId = trimToNull(requestEntry.getMorphId());
            String morphName = trimToNull(requestEntry.getMorphName());

            if (morphId == null && morphName == null) {
                continue;
            }

            if (expressionCategory == null) {
                expressionCategory = EXPRESSION_VISUAL;
            }
            if (!EXPRESSION_VISUAL.equals(expressionCategory) &&
                !EXPRESSION_HET.equals(expressionCategory) &&
                !EXPRESSION_POSSIBLE_HET.equals(expressionCategory)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid expression_category");
            }

            MorphMasterEntity morph = morphId != null
                ? morphMasterMapper.findByPrimaryKey(speciesId, morphId)
                : morphMasterMapper.findBySpeciesIdAndMorphName(speciesId, morphName);
            if (morph == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "morph not found");
            }

            morphId = morph.getMorphId();
            if (!usedMorphIds.add(morphId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "same morph cannot be registered multiple times");
            }

            Integer possibleHetPercentage = requestEntry.getPossibleHetPercentage();
            String bloodlineId = trimToNull(requestEntry.getBloodlineId());
            String bloodlineName = trimToNull(requestEntry.getBloodlineName());
            int sortOrder;

            if (EXPRESSION_VISUAL.equals(expressionCategory)) {
                visualCount += 1;
                sortOrder = 0;
                possibleHetPercentage = null;
            } else {
                if (!MORPH_SINGLE.equals(morph.getMorphType()) || !INHERITANCE_RECESSIVE.equals(morph.getInheritanceCategory())) {
                    throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "het and possible_het can be registered only for recessive morphs"
                    );
                }

                if (EXPRESSION_HET.equals(expressionCategory)) {
                    sortOrder = hetOrder++;
                    possibleHetPercentage = null;
                } else {
                    if (possibleHetPercentage == null) {
                        throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "possible_het_percentage is required for possible_het"
                        );
                    }
                    if (possibleHetPercentage < 0 || possibleHetPercentage > 100) {
                        throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "possible_het_percentage must be between 0 and 100"
                        );
                    }
                    sortOrder = possibleOrder++;
                }
            }

            BloodlineMasterEntity bloodline = null;
            if (!EXPRESSION_VISUAL.equals(expressionCategory) && (bloodlineId != null || bloodlineName != null)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "bloodline can be registered only for the visual morph");
            }
            if (bloodlineId == null && bloodlineName != null) {
                bloodline = bloodlineMasterMapper.findBySpeciesMorphAndBloodlineName(
                    speciesId,
                    morphId,
                    bloodlineName
                );
                bloodlineId = bloodline != null ? bloodline.getBloodlineId() : null;
            }
            if (bloodlineId != null) {
                bloodline = bloodlineMapperFindByPrimaryKey(speciesId, morphId, bloodlineId);
                bloodlineName = bloodline.getBloodlineName();
            } else {
                bloodlineName = null;
            }

            normalizedEntries.add(new NormalizedMorphEntry(
                morph,
                expressionCategory,
                bloodlineId,
                bloodlineName,
                possibleHetPercentage,
                sortOrder
            ));
        }

        if (visualCount != 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "exactly one visual morph is required");
        }

        normalizedEntries.sort(Comparator
            .comparingInt(NormalizedMorphEntry::groupOrder)
            .thenComparingInt(NormalizedMorphEntry::sortOrder));

        IndividualMorph visualMorph = requestEntries.stream()
            .filter(entry -> EXPRESSION_VISUAL.equals(trimToNull(entry.getExpressionCategory())) || trimToNull(entry.getExpressionCategory()) == null)
            .findFirst()
            .orElse(null);

        String visualMorphId = normalizedEntries.stream()
            .filter(entry -> EXPRESSION_VISUAL.equals(entry.expressionCategory))
            .map(entry -> entry.morph.getMorphId())
            .findFirst()
            .orElse(null);

        if (visualMorph != null) {
            visualMorph.setMorphId(visualMorphId);
            visualMorph.setMorphName(normalizedEntries.get(0).morph.getMorphName());
            NormalizedMorphEntry normalizedVisual = normalizedEntries.stream()
                .filter(entry -> EXPRESSION_VISUAL.equals(entry.expressionCategory))
                .findFirst()
                .orElse(null);
            if (normalizedVisual != null) {
                visualMorph.setBloodlineId(normalizedVisual.bloodlineId);
                visualMorph.setBloodlineName(normalizedVisual.bloodlineName);
            }
        }

        List<IndividualMorphEntity> entities = new ArrayList<>();
        for (NormalizedMorphEntry entry : normalizedEntries) {
            IndividualMorphEntity entity = new IndividualMorphEntity();
            entity.setSpeciesId(speciesId);
            entity.setIndividualId(individualId);
            entity.setMorphId(entry.morph.getMorphId());
            entity.setMorphName(entry.morph.getMorphName());
            entity.setBloodlineId(entry.bloodlineId);
            entity.setBloodlineName(entry.bloodlineName);
            entity.setInheritanceCategory(entry.morph.getInheritanceCategory());
            entity.setExpressionCategory(entry.expressionCategory);
            entity.setPossibleHetPercentage(entry.possibleHetPercentage);
            entity.setSortOrder(entry.sortOrder);
            entities.add(entity);
        }
        return entities;
    }

    private String buildMorphDisplay(List<IndividualMorph> morphEntries) {
        if (morphEntries == null || morphEntries.isEmpty()) {
            return null;
        }

        List<NormalizedMorphEntry> entries = new ArrayList<>();
        for (IndividualMorph entry : morphEntries) {
            MorphMasterEntity morph = new MorphMasterEntity();
            morph.setMorphId(entry.getMorphId());
            morph.setMorphName(entry.getMorphName());
            entries.add(new NormalizedMorphEntry(
                morph,
                trimToNull(entry.getExpressionCategory()) != null ? trimToNull(entry.getExpressionCategory()) : EXPRESSION_VISUAL,
                trimToNull(entry.getBloodlineId()),
                trimToNull(entry.getBloodlineName()),
                entry.getPossibleHetPercentage(),
                entry.getSortOrder() != null ? entry.getSortOrder() : 0
            ));
        }
        entries.sort(Comparator
            .comparingInt(NormalizedMorphEntry::groupOrder)
            .thenComparingInt(NormalizedMorphEntry::sortOrder));
        return buildMorphDisplayFromNormalized(entries);
    }

    private String buildMorphDisplayFromNormalized(List<NormalizedMorphEntry> entries) {
        if (entries.isEmpty()) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        NormalizedMorphEntry visual = entries.stream()
            .filter(entry -> EXPRESSION_VISUAL.equals(entry.expressionCategory))
            .findFirst()
            .orElse(null);
        if (visual != null) {
            builder.append(visual.bloodlineName != null ? visual.bloodlineName : visual.morph.getMorphName());
        }

        for (NormalizedMorphEntry entry : entries) {
            if (EXPRESSION_VISUAL.equals(entry.expressionCategory)) {
                continue;
            }
            if (builder.length() > 0) {
                builder.append(' ');
            }
            if (EXPRESSION_HET.equals(entry.expressionCategory)) {
                builder.append("het ").append(entry.morph.getMorphName());
            } else if (EXPRESSION_POSSIBLE_HET.equals(entry.expressionCategory)) {
                builder
                    .append("poss het ")
                    .append(entry.morph.getMorphName())
                    .append(' ')
                    .append(entry.possibleHetPercentage)
                    .append('%');
            }
        }

        return builder.toString();
    }

    private void saveIndividualMorphs(String speciesId, String individualId, List<IndividualMorphEntity> entries, Individual individual) {
        Date createAt = individual.getCreateAt() != null ? individual.getCreateAt() : new Date();
        Date updateAt = individual.getUpdateAt() != null ? individual.getUpdateAt() : createAt;
        String createUser = trimToNull(individual.getCreateUser()) != null ? individual.getCreateUser() : "system";
        String updateUser = trimToNull(individual.getUpdateUser()) != null ? individual.getUpdateUser() : "system";

        for (IndividualMorphEntity entry : entries) {
            entry.setSpeciesId(speciesId);
            entry.setIndividualId(individualId);
            entry.setCreateUser(createUser);
            entry.setCreateAt(createAt);
            entry.setUpdateUser(updateUser);
            entry.setUpdateAt(updateAt);
            individualMorphMapper.insert(entry);
        }
    }

    private void saveIndividualBloodline(
        String speciesId,
        String individualId,
        List<IndividualMorphEntity> entries,
        Individual individual
    ) {
        IndividualMorphEntity visual = entries.stream()
            .filter(entry -> EXPRESSION_VISUAL.equals(entry.getExpressionCategory()))
            .findFirst()
            .orElse(null);
        if (visual == null || trimToNull(visual.getBloodlineId()) == null) {
            return;
        }

        Date createAt = individual.getCreateAt() != null ? individual.getCreateAt() : new Date();
        Date updateAt = individual.getUpdateAt() != null ? individual.getUpdateAt() : createAt;
        IndividualBloodlineEntity bloodline = new IndividualBloodlineEntity();
        bloodline.setSpeciesId(speciesId);
        bloodline.setIndividualId(individualId);
        bloodline.setMorphId(visual.getMorphId());
        bloodline.setBloodlineId(visual.getBloodlineId());
        bloodline.setCreateUser(trimToNull(individual.getCreateUser()) != null ? individual.getCreateUser() : "system");
        bloodline.setCreateAt(createAt);
        bloodline.setUpdateUser(trimToNull(individual.getUpdateUser()) != null ? individual.getUpdateUser() : "system");
        bloodline.setUpdateAt(updateAt);
        individualBloodlineMapper.insert(bloodline);
    }

    private BloodlineMasterEntity bloodlineMapperFindByPrimaryKey(String speciesId, String morphId, String bloodlineId) {
        BloodlineMasterEntity bloodline = bloodlineMasterMapper.findByPrimaryKey(speciesId, morphId, bloodlineId);
        if (bloodline == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "bloodline not found");
        }
        return bloodline;
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

    private record NormalizedMorphEntry(
        MorphMasterEntity morph,
        String expressionCategory,
        String bloodlineId,
        String bloodlineName,
        Integer possibleHetPercentage,
        int sortOrder
    ) {
        private int groupOrder() {
            if (EXPRESSION_VISUAL.equals(expressionCategory)) {
                return 0;
            }
            if (EXPRESSION_HET.equals(expressionCategory)) {
                return 1;
            }
            return 2;
        }
    }
}
