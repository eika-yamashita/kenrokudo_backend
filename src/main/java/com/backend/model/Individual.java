package com.backend.model;

import java.util.Date;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 個体情報
 */

@Schema(name = "Individual", description = "個体情報")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-03T09:16:45.985682500+09:00[Asia/Tokyo]", comments = "Generator version: 7.4.0")
public class Individual {

  private String speciesCd;

  private String id;

  private String maleParentId;

  private String femaleParentId;

  private String morph;

  private String bloodline;

  private String genderCategory;

  private String breedingCategory;

  private String breeder;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private Date clutchDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private Date hatchDate;

  private String purchaseFrom;

  private Double purchasePrice;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private Date purchaseDate;

  private String salesCategory;

  private String salesTo;

  private Double salesPriceTaxEx;

  private Double salesPriceTax;

  private Double salesPriceTaxIn;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private Date salesDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private Date deathDate;

  private String note;

  private String createUser;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Date createAt;

  private String updateUser;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Date updateAt;

  public Individual() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Individual(String speciesCd, String id, String createUser, Date createAt) {
    this.speciesCd = speciesCd;
    this.id = id;
    this.createUser = createUser;
    this.createAt = createAt;
  }

  public Individual speciesCd(String speciesCd) {
    this.speciesCd = speciesCd;
    return this;
  }

  /**
   * 種コード
   * @return speciesCd
  */
  @NotNull @Size(max = 20) 
  @Schema(name = "species_cd", description = "種コード", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("species_cd")
  public String getSpeciesCd() {
    return speciesCd;
  }

  public void setSpeciesCd(String speciesCd) {
    this.speciesCd = speciesCd;
  }

  public Individual id(String id) {
    this.id = id;
    return this;
  }

  /**
   * 個体ID
   * @return id
  */
  @NotNull @Size(max = 20) 
  @Schema(name = "id", description = "個体ID", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Individual maleParentId(String maleParentId) {
    this.maleParentId = maleParentId;
    return this;
  }

  /**
   * オス親個体ID
   * @return maleParentId
  */
  @Size(max = 20) 
  @Schema(name = "male_parent_id", description = "オス親個体ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("male_parent_id")
  public String getMaleParentId() {
    return maleParentId;
  }

  public void setMaleParentId(String maleParentId) {
    this.maleParentId = maleParentId;
  }

  public Individual femaleParentId(String femaleParentId) {
    this.femaleParentId = femaleParentId;
    return this;
  }

  /**
   * メス親個体ID
   * @return femaleParentId
  */
  @Size(max = 20) 
  @Schema(name = "female_parent_id", description = "メス親個体ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("female_parent_id")
  public String getFemaleParentId() {
    return femaleParentId;
  }

  public void setFemaleParentId(String femaleParentId) {
    this.femaleParentId = femaleParentId;
  }

  public Individual morph(String morph) {
    this.morph = morph;
    return this;
  }

  /**
   * モルフ
   * @return morph
  */
  @Size(max = 50) 
  @Schema(name = "morph", description = "モルフ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("morph")
  public String getMorph() {
    return morph;
  }

  public void setMorph(String morph) {
    this.morph = morph;
  }

  public Individual bloodline(String bloodline) {
    this.bloodline = bloodline;
    return this;
  }

  /**
   * 血統
   * @return bloodline
  */
  @Size(max = 50) 
  @Schema(name = "bloodline", description = "血統", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bloodline")
  public String getBloodline() {
    return bloodline;
  }

  public void setBloodline(String bloodline) {
    this.bloodline = bloodline;
  }

  public Individual genderCategory(String genderCategory) {
    this.genderCategory = genderCategory;
    return this;
  }

  /**
   * 雌雄区分（M:オス / F:メス / U:不明）
   * @return genderCategory
  */
  @Size(max = 1) 
  @Schema(name = "gender_category", description = "雌雄区分（M:オス / F:メス / U:不明）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("gender_category")
  public String getGenderCategory() {
    return genderCategory;
  }

  public void setGenderCategory(String genderCategory) {
    this.genderCategory = genderCategory;
  }

  public Individual breedingCategory(String breedingCategory) {
    this.breedingCategory = breedingCategory;
    return this;
  }

  /**
   * 繁殖区分（A:自家繁殖 / B:購入個体）
   * @return breedingCategory
  */
  @Size(max = 1) 
  @Schema(name = "breeding_category", description = "繁殖区分（A:自家繁殖 / B:購入個体）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("breeding_category")
  public String getBreedingCategory() {
    return breedingCategory;
  }

  public void setBreedingCategory(String breedingCategory) {
    this.breedingCategory = breedingCategory;
  }

  public Individual breeder(String breeder) {
    this.breeder = breeder;
    return this;
  }

  /**
   * ブリーダー名
   * @return breeder
  */
  @Size(max = 20) 
  @Schema(name = "breeder", description = "ブリーダー名", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("breeder")
  public String getBreeder() {
    return breeder;
  }

  public void setBreeder(String breeder) {
    this.breeder = breeder;
  }

  public Individual clutchDate(Date clutchDate) {
    this.clutchDate = clutchDate;
    return this;
  }

  /**
   * クラッチ日（産卵日）
   * @return clutchDate
  */
  @Valid 
  @Schema(name = "clutch_date", description = "クラッチ日（産卵日）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("clutch_date")
  public Date getClutchDate() {
    return clutchDate;
  }

  public void setClutchDate(Date clutchDate) {
    this.clutchDate = clutchDate;
  }

  public Individual hatchDate(Date hatchDate) {
    this.hatchDate = hatchDate;
    return this;
  }

  /**
   * ハッチ日（孵化日）
   * @return hatchDate
  */
  @Valid 
  @Schema(name = "hatch_date", description = "ハッチ日（孵化日）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hatch_date")
  public Date getHatchDate() {
    return hatchDate;
  }

  public void setHatchDate(Date hatchDate) {
    this.hatchDate = hatchDate;
  }

  public Individual purchaseFrom(String purchaseFrom) {
    this.purchaseFrom = purchaseFrom;
    return this;
  }

  /**
   * 仕入先
   * @return purchaseFrom
  */
  
  @Schema(name = "purchase_from", description = "仕入先", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("purchase_from")
  public String getPurchaseFrom() {
    return purchaseFrom;
  }

  public void setPurchaseFrom(String purchaseFrom) {
    this.purchaseFrom = purchaseFrom;
  }

  public Individual purchasePrice(Double purchasePrice) {
    this.purchasePrice = purchasePrice;
    return this;
  }

  /**
   * 仕入価格
   * @return purchasePrice
  */
  
  @Schema(name = "purchase_price", description = "仕入価格", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("purchase_price")
  public Double getPurchasePrice() {
    return purchasePrice;
  }

  public void setPurchasePrice(Double purchasePrice) {
    this.purchasePrice = purchasePrice;
  }

  public Individual purchaseDate(Date purchaseDate) {
    this.purchaseDate = purchaseDate;
    return this;
  }

  /**
   * 仕入日
   * @return purchaseDate
  */
  @Valid 
  @Schema(name = "purchase_date", description = "仕入日", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("purchase_date")
  public Date getPurchaseDate() {
    return purchaseDate;
  }

  public void setPurchaseDate(Date purchaseDate) {
    this.purchaseDate = purchaseDate;
  }

  public Individual salesCategory(String salesCategory) {
    this.salesCategory = salesCategory;
    return this;
  }

  /**
   * 販売区分
   * @return salesCategory
  */
  
  @Schema(name = "sales_category", description = "販売区分", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sales_category")
  public String getSalesCategory() {
    return salesCategory;
  }

  public void setSalesCategory(String salesCategory) {
    this.salesCategory = salesCategory;
  }

  public Individual salesTo(String salesTo) {
    this.salesTo = salesTo;
    return this;
  }

  /**
   * 販売先
   * @return salesTo
  */
  
  @Schema(name = "sales_to", description = "販売先", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sales_to")
  public String getSalesTo() {
    return salesTo;
  }

  public void setSalesTo(String salesTo) {
    this.salesTo = salesTo;
  }

  public Individual salesPriceTaxEx(Double salesPriceTaxEx) {
    this.salesPriceTaxEx = salesPriceTaxEx;
    return this;
  }

  /**
   * 販売価格（税抜）
   * @return salesPriceTaxEx
  */
  
  @Schema(name = "sales_price_tax_ex", description = "販売価格（税抜）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sales_price_tax_ex")
  public Double getSalesPriceTaxEx() {
    return salesPriceTaxEx;
  }

  public void setSalesPriceTaxEx(Double salesPriceTaxEx) {
    this.salesPriceTaxEx = salesPriceTaxEx;
  }

  public Individual salesPriceTax(Double salesPriceTax) {
    this.salesPriceTax = salesPriceTax;
    return this;
  }

  /**
   * 消費税額
   * @return salesPriceTax
  */
  
  @Schema(name = "sales_price_tax", description = "消費税額", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sales_price_tax")
  public Double getSalesPriceTax() {
    return salesPriceTax;
  }

  public void setSalesPriceTax(Double salesPriceTax) {
    this.salesPriceTax = salesPriceTax;
  }

  public Individual salesPriceTaxIn(Double salesPriceTaxIn) {
    this.salesPriceTaxIn = salesPriceTaxIn;
    return this;
  }

  /**
   * 販売価格（税込）
   * @return salesPriceTaxIn
  */
  
  @Schema(name = "sales_price_tax_in", description = "販売価格（税込）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sales_price_tax_in")
  public Double getSalesPriceTaxIn() {
    return salesPriceTaxIn;
  }

  public void setSalesPriceTaxIn(Double salesPriceTaxIn) {
    this.salesPriceTaxIn = salesPriceTaxIn;
  }

  public Individual salesDate(Date salesDate) {
    this.salesDate = salesDate;
    return this;
  }

  /**
   * 販売日
   * @return salesDate
  */
  @Valid 
  @Schema(name = "sales_date", description = "販売日", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sales_date")
  public Date getSalesDate() {
    return salesDate;
  }

  public void setSalesDate(Date salesDate) {
    this.salesDate = salesDate;
  }

  public Individual deathDate(Date deathDate) {
    this.deathDate = deathDate;
    return this;
  }

  /**
   * 死亡日
   * @return deathDate
  */
  @Valid 
  @Schema(name = "death_date", description = "死亡日", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("death_date")
  public Date getDeathDate() {
    return deathDate;
  }

  public void setDeathDate(Date deathDate) {
    this.deathDate = deathDate;
  }

  public Individual note(String note) {
    this.note = note;
    return this;
  }

  /**
   * 備考
   * @return note
  */
  @Size(max = 255) 
  @Schema(name = "note", description = "備考", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("note")
  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public Individual createUser(String createUser) {
    this.createUser = createUser;
    return this;
  }

  /**
   * 登録者
   * @return createUser
  */
  @NotNull @Size(max = 20) 
  @Schema(name = "create_user", description = "登録者", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("create_user")
  public String getCreateUser() {
    return createUser;
  }

  public void setCreateUser(String createUser) {
    this.createUser = createUser;
  }

  public Individual createAt(Date createAt) {
    this.createAt = createAt;
    return this;
  }

  /**
   * 登録日時
   * @return createAt
  */
  @NotNull @Valid 
  @Schema(name = "create_at", description = "登録日時", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("create_at")
  public Date getCreateAt() {
    return createAt;
  }

  public void setCreateAt(Date createAt) {
    this.createAt = createAt;
  }

  public Individual updateUser(String updateUser) {
    this.updateUser = updateUser;
    return this;
  }

  /**
   * 更新者
   * @return updateUser
  */
  @Size(max = 20) 
  @Schema(name = "update_user", description = "更新者", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("update_user")
  public String getUpdateUser() {
    return updateUser;
  }

  public void setUpdateUser(String updateUser) {
    this.updateUser = updateUser;
  }

  public Individual updateAt(Date updateAt) {
    this.updateAt = updateAt;
    return this;
  }

  /**
   * 更新日時
   * @return updateAt
  */
  @Valid 
  @Schema(name = "update_at", description = "更新日時", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("update_at")
  public Date getUpdateAt() {
    return updateAt;
  }

  public void setUpdateAt(Date updateAt) {
    this.updateAt = updateAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Individual individual = (Individual) o;
    return Objects.equals(this.speciesCd, individual.speciesCd) &&
        Objects.equals(this.id, individual.id) &&
        Objects.equals(this.maleParentId, individual.maleParentId) &&
        Objects.equals(this.femaleParentId, individual.femaleParentId) &&
        Objects.equals(this.morph, individual.morph) &&
        Objects.equals(this.bloodline, individual.bloodline) &&
        Objects.equals(this.genderCategory, individual.genderCategory) &&
        Objects.equals(this.breedingCategory, individual.breedingCategory) &&
        Objects.equals(this.breeder, individual.breeder) &&
        Objects.equals(this.clutchDate, individual.clutchDate) &&
        Objects.equals(this.hatchDate, individual.hatchDate) &&
        Objects.equals(this.purchaseFrom, individual.purchaseFrom) &&
        Objects.equals(this.purchasePrice, individual.purchasePrice) &&
        Objects.equals(this.purchaseDate, individual.purchaseDate) &&
        Objects.equals(this.salesCategory, individual.salesCategory) &&
        Objects.equals(this.salesTo, individual.salesTo) &&
        Objects.equals(this.salesPriceTaxEx, individual.salesPriceTaxEx) &&
        Objects.equals(this.salesPriceTax, individual.salesPriceTax) &&
        Objects.equals(this.salesPriceTaxIn, individual.salesPriceTaxIn) &&
        Objects.equals(this.salesDate, individual.salesDate) &&
        Objects.equals(this.deathDate, individual.deathDate) &&
        Objects.equals(this.note, individual.note) &&
        Objects.equals(this.createUser, individual.createUser) &&
        Objects.equals(this.createAt, individual.createAt) &&
        Objects.equals(this.updateUser, individual.updateUser) &&
        Objects.equals(this.updateAt, individual.updateAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(speciesCd, id, maleParentId, femaleParentId, morph, bloodline, genderCategory, breedingCategory, breeder, clutchDate, hatchDate, purchaseFrom, purchasePrice, purchaseDate, salesCategory, salesTo, salesPriceTaxEx, salesPriceTax, salesPriceTaxIn, salesDate, deathDate, note, createUser, createAt, updateUser, updateAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Individual {\n");
    sb.append("    speciesCd: ").append(toIndentedString(speciesCd)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    maleParentId: ").append(toIndentedString(maleParentId)).append("\n");
    sb.append("    femaleParentId: ").append(toIndentedString(femaleParentId)).append("\n");
    sb.append("    morph: ").append(toIndentedString(morph)).append("\n");
    sb.append("    bloodline: ").append(toIndentedString(bloodline)).append("\n");
    sb.append("    genderCategory: ").append(toIndentedString(genderCategory)).append("\n");
    sb.append("    breedingCategory: ").append(toIndentedString(breedingCategory)).append("\n");
    sb.append("    breeder: ").append(toIndentedString(breeder)).append("\n");
    sb.append("    clutchDate: ").append(toIndentedString(clutchDate)).append("\n");
    sb.append("    hatchDate: ").append(toIndentedString(hatchDate)).append("\n");
    sb.append("    purchaseFrom: ").append(toIndentedString(purchaseFrom)).append("\n");
    sb.append("    purchasePrice: ").append(toIndentedString(purchasePrice)).append("\n");
    sb.append("    purchaseDate: ").append(toIndentedString(purchaseDate)).append("\n");
    sb.append("    salesCategory: ").append(toIndentedString(salesCategory)).append("\n");
    sb.append("    salesTo: ").append(toIndentedString(salesTo)).append("\n");
    sb.append("    salesPriceTaxEx: ").append(toIndentedString(salesPriceTaxEx)).append("\n");
    sb.append("    salesPriceTax: ").append(toIndentedString(salesPriceTax)).append("\n");
    sb.append("    salesPriceTaxIn: ").append(toIndentedString(salesPriceTaxIn)).append("\n");
    sb.append("    salesDate: ").append(toIndentedString(salesDate)).append("\n");
    sb.append("    deathDate: ").append(toIndentedString(deathDate)).append("\n");
    sb.append("    note: ").append(toIndentedString(note)).append("\n");
    sb.append("    createUser: ").append(toIndentedString(createUser)).append("\n");
    sb.append("    createAt: ").append(toIndentedString(createAt)).append("\n");
    sb.append("    updateUser: ").append(toIndentedString(updateUser)).append("\n");
    sb.append("    updateAt: ").append(toIndentedString(updateAt)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

