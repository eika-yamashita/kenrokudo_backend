package com.backend.service;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.backend.entity.ExhibitionEventEntity;
import com.backend.mapper.ExhibitionEventMapper;

@Service
public class ExhibitionEventService {

    private static final String SYSTEM_USER = "system";
    private static final int DEFAULT_UPCOMING_LIMIT = 3;
    private static final int MAX_UPCOMING_LIMIT = 20;
    private static final Pattern TIME_PATTERN = Pattern.compile("^([01][0-9]|2[0-3]):[0-5][0-9]$");

    private final ExhibitionEventMapper exhibitionEventMapper;

    public ExhibitionEventService(ExhibitionEventMapper exhibitionEventMapper) {
        this.exhibitionEventMapper = exhibitionEventMapper;
    }

    public List<ExhibitionEventEntity> getEvents() {
        return exhibitionEventMapper.findAll();
    }

    public List<ExhibitionEventEntity> getUpcomingPublished(Integer limit) {
        int normalizedLimit = limit == null ? DEFAULT_UPCOMING_LIMIT : Math.max(1, Math.min(limit, MAX_UPCOMING_LIMIT));
        return exhibitionEventMapper.findUpcomingPublished(normalizedLimit);
    }

    public ExhibitionEventEntity getEvent(Long eventId) {
        ExhibitionEventEntity event = exhibitionEventMapper.findByEventId(requireEventId(eventId));
        if (event == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "イベントが見つかりません");
        }
        return event;
    }

    @Transactional
    public ExhibitionEventEntity createEvent(ExhibitionEventEntity request) {
        ExhibitionEventEntity normalized = normalize(request, true);
        exhibitionEventMapper.insert(normalized);
        return getEvent(normalized.getEventId());
    }

    @Transactional
    public ExhibitionEventEntity updateEvent(Long eventId, ExhibitionEventEntity request) {
        getEvent(eventId);
        ExhibitionEventEntity normalized = normalize(request, false);
        int updated = exhibitionEventMapper.update(eventId, normalized);
        if (updated == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "イベントが見つかりません");
        }
        return getEvent(eventId);
    }

    @Transactional
    public void deleteEvent(Long eventId) {
        int deleted = exhibitionEventMapper.delete(requireEventId(eventId));
        if (deleted == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "イベントが見つかりません");
        }
    }

    private ExhibitionEventEntity normalize(ExhibitionEventEntity request, boolean create) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "リクエスト内容が指定されていません");
        }

        ExhibitionEventEntity normalized = new ExhibitionEventEntity();
        normalized.setEventName(requireNonBlank(request.getEventName(), "イベント名は必須です"));
        if (request.getEventDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "イベント日は必須です");
        }
        normalized.setEventDate(request.getEventDate());
        normalized.setArea(trimToNull(request.getArea()));
        normalized.setStartTime(validateTime(trimToNull(request.getStartTime()), "開始時間"));
        normalized.setEndTime(validateTime(trimToNull(request.getEndTime()), "終了時間"));
        normalized.setVenue(trimToNull(request.getVenue()));
        normalized.setEventUrl(trimToNull(request.getEventUrl()));
        normalized.setNote(trimToNull(request.getNote()));
        normalized.setDisplayOrder(request.getDisplayOrder() == null ? 0 : request.getDisplayOrder());
        normalized.setIsPublished(request.getIsPublished() == null ? Boolean.TRUE : request.getIsPublished());
        if (create) {
            normalized.setCreateUser(SYSTEM_USER);
        }
        normalized.setUpdateUser(SYSTEM_USER);
        return normalized;
    }

    private Long requireEventId(Long eventId) {
        if (eventId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "イベントIDは必須です");
        }
        return eventId;
    }

    private String validateTime(String value, String label) {
        if (value == null) {
            return null;
        }
        if (!TIME_PATTERN.matcher(value).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, label + "は HH:mm 形式で指定してください");
        }
        return value;
    }

    private String requireNonBlank(String value, String message) {
        String normalized = trimToNull(value);
        if (normalized == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
        return normalized;
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
