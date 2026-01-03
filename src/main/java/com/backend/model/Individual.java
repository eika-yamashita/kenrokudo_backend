package com.backend.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import java.util.Date;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * 個体情報
 */

@Schema(name = "Individual", description = "個体情報")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-03T01:08:01.089826924Z[Etc/UTC]", comments = "Generator version: 7.4.0")
public class Individual {

  private String speciesCd;

  private String id;

  private JsonNullable<@Size(max = 20) String> maleParentId = JsonNullable.<String>undefined();

  private JsonNullable<@Size(max = 20) String> femaleParentId = JsonNullable.<String>undefined();

  private JsonNullable<@Size(max = 50) String> morph = JsonNullable.<String>undefined();

  private JsonNullable<@Size(max = 50) String> bloodline = JsonNullable.<String>undefined();

  private JsonNullable<@Size(max = 1) String> genderCategory = JsonNullable.<String>undefined();

  private JsonNullable<@Size(max = 1) String> breedingCategory = JsonNullable.<String>undefined();

  private JsonNullable<@Size(max = 20) String> breeder = JsonNullable.<String>undefined();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private JsonNullable<Date> clutchDate = JsonNullable.<Date>undefined();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private JsonNullable<Date> hatchDate = JsonNullable.<Date>undefined();

  private JsonNullable<String> purchaseFrom = JsonNullable.<String>undefined();

  private JsonNullable<Double> purchasePrice = JsonNullable.<Double>undefined();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private JsonNullable<Date> purchaseDate = JsonNullable.<Date>undefined();

  private JsonNullable<String> salesCategory = JsonNullable.<String>undefined();

  private JsonNullable<String> salesTo = JsonNullable.<String>undefined();

  private JsonNullable<Double> salesPriceTaxEx = JsonNullable.<Double>undefined();

  private JsonNullable<Double> salesPriceTax = JsonNullable.<Double>undefined();

  private JsonNullable<Double> salesPriceTaxIn = JsonNullable.<Double>undefined();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private JsonNullable<Date> salesDate = JsonNullable.<Date>undefined();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private JsonNullable<Date> deathDate = JsonNullable.<Date>undefined();

  private JsonNullable<@Size(max = 255) String> note = JsonNullable.<String>undefined();

  private String createUser;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Date createAt;

  private JsonNullable<@Size(max = 20) String> updateUser = JsonNullable.<String>undefined();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<Date> updateAt = JsonNullable.<Date>undefined();

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
    this.maleParentId = JsonNullable.of(maleParentId);
    return this;
  }

  /**
   * オス親個体ID
   * @return maleParentId
  */
  @Size(max = 20) 
  @Schema(name = "male_parent_id", description = "オス親個体ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("male_parent_id")
  public JsonNullable<@Size(max = 20) String> getMaleParentId() {
    return maleParentId;
  }

  public void setMaleParentId(JsonNullable<String> maleParentId) {
    this.maleParentId = maleParentId;
  }

  public Individual femaleParentId(String femaleParentId) {
    this.femaleParentId = JsonNullable.of(femaleParentId);
    return this;
  }

  /**
   * メス親個体ID
   * @return femaleParentId
  */
  @Size(max = 20) 
  @Schema(name = "female_parent_id", description = "メス親個体ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("female_parent_id")
  public JsonNullable<@Size(max = 20) String> getFemaleParentId() {
    return femaleParentId;
  }

  public void setFemaleParentId(JsonNullable<String> femaleParentId) {
    this.femaleParentId = femaleParentId;
  }

  public Individual morph(String morph) {
    this.morph = JsonNullable.of(morph);
    return this;
  }

  /**
   * モルフ
   * @return morph
  */
  @Size(max = 50) 
  @Schema(name = "morph", description = "モルフ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("morph")
  public JsonNullable<@Size(max = 50) String> getMorph() {
    return morph;
  }

  public void setMorph(JsonNullable<String> morph) {
    this.morph = morph;
  }

  public Individual bloodline(String bloodline) {
    this.bloodline = JsonNullable.of(bloodline);
    return this;
  }

  /**
   * 血統
   * @return bloodline
  */
  @Size(max = 50) 
  @Schema(name = "bloodline", description = "血統", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bloodline")
  public JsonNullable<@Size(max = 50) String> getBloodline() {
    return bloodline;
  }

  public void setBloodline(JsonNullable<String> bloodline) {
    this.bloodline = bloodline;
  }

  public Individual genderCategory(String genderCategory) {
    this.genderCategory = JsonNullable.of(genderCategory);
    return this;
  }

  /**
   * 雌雄区分（M:オス / F:メス / U:不明）
   * @return genderCategory
  */
  @Size(max = 1) 
  @Schema(name = "gender_category", description = "雌雄区分（M:オス / F:メス / U:不明）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("gender_category")
  public JsonNullable<@Size(max = 1) String> getGenderCategory() {
    return genderCategory;
  }

  public void setGenderCategory(JsonNullable<String> genderCategory) {
    this.genderCategory = genderCategory;
  }

  public Individual breedingCategory(String breedingCategory) {
    this.breedingCategory = JsonNullable.of(breedingCategory);
    return this;
  }

  /**
   * 繁殖区分（A:自家繁殖 / B:購入個体）
   * @return breedingCategory
  */
  @Size(max = 1) 
  @Schema(name = "breeding_category", description = "繁殖区分（A:自家繁殖 / B:購入個体）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("breeding_category")
  public JsonNullable<@Size(max = 1) String> getBreedingCategory() {
    return breedingCategory;
  }

  public void setBreedingCategory(JsonNullable<String> breedingCategory) {
    this.breedingCategory = breedingCategory;
  }

  public Individual breeder(String breeder) {
    this.breeder = JsonNullable.of(breeder);
    return this;
  }

  /**
   * ブリーダー名
   * @return breeder
  */
  @Size(max = 20) 
  @Schema(name = "breeder", description = "ブリーダー名", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("breeder")
  public JsonNullable<@Size(max = 20) String> getBreeder() {
    return breeder;
  }

  public void setBreeder(JsonNullable<String> breeder) {
    this.breeder = breeder;
  }

  public Individual clutchDate(Date clutchDate) {
    this.clutchDate = JsonNullable.of(clutchDate);
    return this;
  }

  /**
   * クラッチ日（産卵日）
   * @return clutchDate
  */
  @Valid 
  @Schema(name = "clutch_date", description = "クラッチ日（産卵日）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("clutch_date")
  public JsonNullable<Date> getClutchDate() {
    return clutchDate;
  }

  public void setClutchDate(JsonNullable<Date> clutchDate) {
    this.clutchDate = clutchDate;
  }

  public Individual hatchDate(Date hatchDate) {
    this.hatchDate = JsonNullable.of(hatchDate);
    return this;
  }

  /**
   * ハッチ日（孵化日）
   * @return hatchDate
  */
  @Valid 
  @Schema(name = "hatch_date", description = "ハッチ日（孵化日）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hatch_date")
  public JsonNullable<Date> getHatchDate() {
    return hatchDate;
  }

  public void setHatchDate(JsonNullable<Date> hatchDate) {
    this.hatchDate = hatchDate;
  }

  public Individual purchaseFrom(String purchaseFrom) {
    this.purchaseFrom = JsonNullable.of(purchaseFrom);
    return this;
  }

  /**
   * 仕入先
   * @return purchaseFrom
  */
  
  @Schema(name = "purchase_from", description = "仕入先", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("purchase_from")
  public JsonNullable<String> getPurchaseFrom() {
    return purchaseFrom;
  }

  public void setPurchaseFrom(JsonNullable<String> purchaseFrom) {
    this.purchaseFrom = purchaseFrom;
  }

  public Individual purchasePrice(Double purchasePrice) {
    this.purchasePrice = JsonNullable.of(purchasePrice);
    return this;
  }

  /**
   * 仕入価格
   * @return purchasePrice
  */
  
  @Schema(name = "purchase_price", description = "仕入価格", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("purchase_price")
  public JsonNullable<Double> getPurchasePrice() {
    return purchasePrice;
  }

  public void setPurchasePrice(JsonNullable<Double> purchasePrice) {
    this.purchasePrice = purchasePrice;
  }

  public Individual purchaseDate(Date purchaseDate) {
    this.purchaseDate = JsonNullable.of(purchaseDate);
    return this;
  }

  /**
   * 仕入日
   * @return purchaseDate
  */
  @Valid 
  @Schema(name = "purchase_date", description = "仕入日", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("purchase_date")
  public JsonNullable<Date> getPurchaseDate() {
    return purchaseDate;
  }

  public void setPurchaseDate(JsonNullable<Date> purchaseDate) {
    this.purchaseDate = purchaseDate;
  }

  public Individual salesCategory(String salesCategory) {
    this.salesCategory = JsonNullable.of(salesCategory);
    return this;
  }

  /**
   * 販売区分
   * @return salesCategory
  */
  
  @Schema(name = "sales_category", description = "販売区分", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sales_category")
  public JsonNullable<String> getSalesCategory() {
    return salesCategory;
  }

  public void setSalesCategory(JsonNullable<String> salesCategory) {
    this.salesCategory = salesCategory;
  }

  public Individual salesTo(String salesTo) {
    this.salesTo = JsonNullable.of(salesTo);
    return this;
  }

  /**
   * 販売先
   * @return salesTo
  */
  
  @Schema(name = "sales_to", description = "販売先", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sales_to")
  public JsonNullable<String> getSalesTo() {
    return salesTo;
  }

  public void setSalesTo(JsonNullable<String> salesTo) {
    this.salesTo = salesTo;
  }

  public Individual salesPriceTaxEx(Double salesPriceTaxEx) {
    this.salesPriceTaxEx = JsonNullable.of(salesPriceTaxEx);
    return this;
  }

  /**
   * 販売価格（税抜）
   * @return salesPriceTaxEx
  */
  
  @Schema(name = "sales_price_tax_ex", description = "販売価格（税抜）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sales_price_tax_ex")
  public JsonNullable<Double> getSalesPriceTaxEx() {
    return salesPriceTaxEx;
  }

  public void setSalesPriceTaxEx(JsonNullable<Double> salesPriceTaxEx) {
    this.salesPriceTaxEx = salesPriceTaxEx;
  }

  public Individual salesPriceTax(Double salesPriceTax) {
    this.salesPriceTax = JsonNullable.of(salesPriceTax);
    return this;
  }

  /**
   * 消費税額
   * @return salesPriceTax
  */
  
  @Schema(name = "sales_price_tax", description = "消費税額", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sales_price_tax")
  public JsonNullable<Double> getSalesPriceTax() {
    return salesPriceTax;
  }

  public void setSalesPriceTax(JsonNullable<Double> salesPriceTax) {
    this.salesPriceTax = salesPriceTax;
  }

  public Individual salesPriceTaxIn(Double salesPriceTaxIn) {
    this.salesPriceTaxIn = JsonNullable.of(salesPriceTaxIn);
    return this;
  }

  /**
   * 販売価格（税込）
   * @return salesPriceTaxIn
  */
  
  @Schema(name = "sales_price_tax_in", description = "販売価格（税込）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sales_price_tax_in")
  public JsonNullable<Double> getSalesPriceTaxIn() {
    return salesPriceTaxIn;
  }

  public void setSalesPriceTaxIn(JsonNullable<Double> salesPriceTaxIn) {
    this.salesPriceTaxIn = salesPriceTaxIn;
  }

  public Individual salesDate(Date salesDate) {
    this.salesDate = JsonNullable.of(salesDate);
    return this;
  }

  /**
   * 販売日
   * @return salesDate
  */
  @Valid 
  @Schema(name = "sales_date", description = "販売日", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sales_date")
  public JsonNullable<Date> getSalesDate() {
    return salesDate;
  }

  public void setSalesDate(JsonNullable<Date> salesDate) {
    this.salesDate = salesDate;
  }

  public Individual deathDate(Date deathDate) {
    this.deathDate = JsonNullable.of(deathDate);
    return this;
  }

  /**
   * 死亡日
   * @return deathDate
  */
  @Valid 
  @Schema(name = "death_date", description = "死亡日", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("death_date")
  public JsonNullable<Date> getDeathDate() {
    return deathDate;
  }

  public void setDeathDate(JsonNullable<Date> deathDate) {
    this.deathDate = deathDate;
  }

  public Individual note(String note) {
    this.note = JsonNullable.of(note);
    return this;
  }

  /**
   * 備考
   * @return note
  */
  @Size(max = 255) 
  @Schema(name = "note", description = "備考", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("note")
  public JsonNullable<@Size(max = 255) String> getNote() {
    return note;
  }

  public void setNote(JsonNullable<String> note) {
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
    this.updateUser = JsonNullable.of(updateUser);
    return this;
  }

  /**
   * 更新者
   * @return updateUser
  */
  @Size(max = 20) 
  @Schema(name = "update_user", description = "更新者", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("update_user")
  public JsonNullable<@Size(max = 20) String> getUpdateUser() {
    return updateUser;
  }

  public void setUpdateUser(JsonNullable<String> updateUser) {
    this.updateUser = updateUser;
  }

  public Individual updateAt(Date updateAt) {
    this.updateAt = JsonNullable.of(updateAt);
    return this;
  }

  /**
   * 更新日時
   * @return updateAt
  */
  @Valid 
  @Schema(name = "update_at", description = "更新日時", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("update_at")
  public JsonNullable<Date> getUpdateAt() {
    return updateAt;
  }

  public void setUpdateAt(JsonNullable<Date> updateAt) {
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
        equalsNullable(this.maleParentId, individual.maleParentId) &&
        equalsNullable(this.femaleParentId, individual.femaleParentId) &&
        equalsNullable(this.morph, individual.morph) &&
        equalsNullable(this.bloodline, individual.bloodline) &&
        equalsNullable(this.genderCategory, individual.genderCategory) &&
        equalsNullable(this.breedingCategory, individual.breedingCategory) &&
        equalsNullable(this.breeder, individual.breeder) &&
        equalsNullable(this.clutchDate, individual.clutchDate) &&
        equalsNullable(this.hatchDate, individual.hatchDate) &&
        equalsNullable(this.purchaseFrom, individual.purchaseFrom) &&
        equalsNullable(this.purchasePrice, individual.purchasePrice) &&
        equalsNullable(this.purchaseDate, individual.purchaseDate) &&
        equalsNullable(this.salesCategory, individual.salesCategory) &&
        equalsNullable(this.salesTo, individual.salesTo) &&
        equalsNullable(this.salesPriceTaxEx, individual.salesPriceTaxEx) &&
        equalsNullable(this.salesPriceTax, individual.salesPriceTax) &&
        equalsNullable(this.salesPriceTaxIn, individual.salesPriceTaxIn) &&
        equalsNullable(this.salesDate, individual.salesDate) &&
        equalsNullable(this.deathDate, individual.deathDate) &&
        equalsNullable(this.note, individual.note) &&
        Objects.equals(this.createUser, individual.createUser) &&
        Objects.equals(this.createAt, individual.createAt) &&
        equalsNullable(this.updateUser, individual.updateUser) &&
        equalsNullable(this.updateAt, individual.updateAt);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(speciesCd, id, hashCodeNullable(maleParentId), hashCodeNullable(femaleParentId), hashCodeNullable(morph), hashCodeNullable(bloodline), hashCodeNullable(genderCategory), hashCodeNullable(breedingCategory), hashCodeNullable(breeder), hashCodeNullable(clutchDate), hashCodeNullable(hatchDate), hashCodeNullable(purchaseFrom), hashCodeNullable(purchasePrice), hashCodeNullable(purchaseDate), hashCodeNullable(salesCategory), hashCodeNullable(salesTo), hashCodeNullable(salesPriceTaxEx), hashCodeNullable(salesPriceTax), hashCodeNullable(salesPriceTaxIn), hashCodeNullable(salesDate), hashCodeNullable(deathDate), hashCodeNullable(note), createUser, createAt, hashCodeNullable(updateUser), hashCodeNullable(updateAt));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
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

