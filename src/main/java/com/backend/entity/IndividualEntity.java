package com.backend.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class IndividualEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("species_id")
    private String speciesId;

    @JsonProperty("fiscal_year")
    private Integer fiscalYear;

    @JsonProperty("id")
    private String id;

    @JsonProperty("pairing_fiscal_year")
    private Integer pairingFiscalYear;

    @JsonProperty("pairing_id")
    private String pairingId;

    @JsonProperty("male_parent_id")
    private String maleParentId;

    @JsonProperty("female_parent_id")
    private String femaleParentId;

    @JsonProperty("gender_category")
    private String genderCategory;

    @JsonProperty("breeding_category")
    private String breedingCategory;

    @JsonProperty("breeder")
    private String breeder;

    @JsonProperty("clutch_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date clutchDate;

    @JsonProperty("hatch_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date hatchDate;

    @JsonProperty("purchase_from")
    private String purchaseFrom;

    @JsonProperty("purchase_price")
    private Double purchasePrice;

    @JsonProperty("purchase_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date purchaseDate;

    @JsonProperty("sales_category")
    private String salesCategory;

    @JsonProperty("sales_to")
    private String salesTo;

    @JsonProperty("sales_price_tax_ex")
    private Double salesPriceTaxEx;

    @JsonProperty("sales_price_tax")
    private Double salesPriceTax;

    @JsonProperty("sales_price_tax_in")
    private Double salesPriceTaxIn;

    @JsonProperty("sales_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date salesDate;

    @JsonProperty("death_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deathDate;

    @JsonProperty("note")
    private String note;

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
