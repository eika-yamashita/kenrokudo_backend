package com.backend.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Individual implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 種コード */
    @JsonProperty("species_cd")
    private String speciesCd;

    /** 個体ID */
    @JsonProperty("id")
    private String id;

    /** オス親個体ID */
    @JsonProperty("male_parent_id")
    private String maleParentId;

    /** メス親個体ID */
    @JsonProperty("female_parent_id")
    private String femaleParentId;

    /** モルフ */
    @JsonProperty("morph")
    private String morph;

    /** 血統 */
    @JsonProperty("bloodline")
    private String bloodline;

    /** 雌雄区分 */
    @JsonProperty("gender_category")
    private String genderCategory;

    /** 繁殖区分 */
    @JsonProperty("breeding_category")
    private String breedingCategory;

    /** ブリーダー名 */
    @JsonProperty("breeder")
    private String breeder;

    /** クラッチ日（産卵日） */
    @JsonProperty("clutch_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date clutchDate;

    /** ハッチ日（孵化日） */
    @JsonProperty("hatch_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date hatchDate;

    /** 仕入先 */
    @JsonProperty("purchase_from")
    private String purchaseFrom;

    /** 仕入価格 */
    @JsonProperty("purchase_price")
    private Double purchasePrice;

    /** 仕入日 */
    @JsonProperty("purchase_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date purchaseDate;

    /** 販売区分 */
    @JsonProperty("sales_category")
    private String salesCategory;

    /** 販売先 */
    @JsonProperty("sales_to")
    private String salesTo;

    /** 販売価格（税抜） */
    @JsonProperty("sales_price_tax_ex")
    private Double salesPriceTaxEx;

    /** 消費税額 */
    @JsonProperty("sales_price_tax")
    private Double salesPriceTax;

    /** 販売価格（税込） */
    @JsonProperty("sales_price_tax_in")
    private Double salesPriceTaxIn;

    /** 販売日 */
    @JsonProperty("sales_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date salesDate;

    /** 死亡日 */
    @JsonProperty("death_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deathDate;

    /** 備考 */
    @JsonProperty("note")
    private String note;

    /** 登録者 */
    @JsonProperty("create_user")
    private String createUser;

    /** 登録日時 */
    @JsonProperty("create_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private Date createAt;

    /** 更新者 */
    @JsonProperty("update_user")
    private String updateUser;

    /** 更新日時 */
    @JsonProperty("update_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private Date updateAt;
}
