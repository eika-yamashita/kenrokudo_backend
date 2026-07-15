package com.backend.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ExhibitionEventEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("event_id")
    private Long eventId;

    @JsonProperty("event_name")
    private String eventName;

    @JsonProperty("event_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date eventDate;

    @JsonProperty("area")
    private String area;

    @JsonProperty("start_time")
    private String startTime;

    @JsonProperty("end_time")
    private String endTime;

    @JsonProperty("venue")
    private String venue;

    @JsonProperty("event_url")
    private String eventUrl;

    @JsonProperty("note")
    private String note;

    @JsonProperty("display_order")
    private Integer displayOrder;

    @JsonProperty("is_published")
    private Boolean isPublished;

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
