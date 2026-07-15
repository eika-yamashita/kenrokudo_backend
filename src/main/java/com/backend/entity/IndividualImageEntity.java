package com.backend.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class IndividualImageEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("image_id")
    private Long imageId;

    @JsonProperty("species_id")
    private String speciesId;

    @JsonProperty("individual_id")
    private String individualId;

    @JsonProperty("storage_path")
    private String storagePath;

    @JsonProperty("public_url")
    private String publicUrl;

    @JsonProperty("file_name")
    private String fileName;

    @JsonProperty("content_type")
    private String contentType;

    @JsonProperty("file_size")
    private Long fileSize;

    @JsonProperty("sort_order")
    private Integer sortOrder;

    @JsonProperty("is_primary")
    private Boolean isPrimary;

    @JsonProperty("create_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private Date createAt;

    @JsonProperty("create_user")
    private String createUser;

    @JsonProperty("update_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private Date updateAt;

    @JsonProperty("update_user")
    private String updateUser;
}
