package com.backend.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class IndividualMorphEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("species_id")
    private String speciesId;

    @JsonProperty("individual_id")
    private String individualId;

    @JsonProperty("morph_id")
    private String morphId;

    @JsonProperty("morph_name")
    private String morphName;

    @JsonProperty("bloodline_id")
    private String bloodlineId;

    @JsonProperty("bloodline_name")
    private String bloodlineName;

    @JsonProperty("inheritance_category")
    private String inheritanceCategory;

    @JsonProperty("expression_category")
    private String expressionCategory;

    @JsonProperty("possible_het_percentage")
    private Integer possibleHetPercentage;

    @JsonProperty("sort_order")
    private Integer sortOrder;

    @JsonProperty("create_user")
    private String createUser;

    @JsonProperty("create_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private Date createAt;

    @JsonProperty("update_user")
    private String updateUser;

    @JsonProperty("update_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private Date updateAt;
}
