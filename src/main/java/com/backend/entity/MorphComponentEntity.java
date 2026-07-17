package com.backend.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MorphComponentEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("species_id")
    private String speciesId;

    @JsonProperty("combo_morph_id")
    private String comboMorphId;

    @JsonProperty("component_morph_id")
    private String componentMorphId;

    @JsonProperty("component_morph_name")
    private String componentMorphName;

    @JsonProperty("required_expression")
    private String requiredExpression;

    @JsonProperty("sort_order")
    private Integer sortOrder;
}
