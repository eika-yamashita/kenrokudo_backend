package com.backend.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MorphMasterEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("species_id")
    private String speciesId;

    @JsonProperty("morph_id")
    private String morphId;

    @JsonProperty("morph_name")
    private String morphName;

    @JsonProperty("inheritance_category")
    private String inheritanceCategory;
}
