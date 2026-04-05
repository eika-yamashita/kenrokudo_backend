package com.backend.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PairingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("species_id")
    private String speciesId;

    @JsonProperty("fiscal_year")
    private Integer fiscalYear;

    @JsonProperty("pairing_id")
    private String pairingId;

    @JsonProperty("male_parent_id")
    private String maleParentId;

    @JsonProperty("female_parent_id")
    private String femaleParentId;

    @JsonProperty("pairing_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date pairingDate;

    @JsonProperty("note")
    private String note;
}
