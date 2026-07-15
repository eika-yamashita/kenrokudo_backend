package com.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.entity.ExhibitionEventEntity;
import com.backend.service.ExhibitionEventService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
public class ExhibitionEventController {

    private final ExhibitionEventService exhibitionEventService;

    public ExhibitionEventController(ExhibitionEventService exhibitionEventService) {
        this.exhibitionEventService = exhibitionEventService;
    }

    @GetMapping("/events")
    public ResponseEntity<List<ExhibitionEventEntity>> getEvents() {
        return ResponseEntity.ok(exhibitionEventService.getEvents());
    }

    @GetMapping("/public/events/upcoming")
    public ResponseEntity<List<ExhibitionEventEntity>> getUpcomingEvents(
        @RequestParam(value = "limit", required = false) Integer limit
    ) {
        return ResponseEntity.ok(exhibitionEventService.getUpcomingPublished(limit));
    }

    @GetMapping("/events/{event_id}")
    public ResponseEntity<ExhibitionEventEntity> getEvent(@PathVariable("event_id") Long eventId) {
        return ResponseEntity.ok(exhibitionEventService.getEvent(eventId));
    }

    @PostMapping("/events")
    public ResponseEntity<ExhibitionEventEntity> createEvent(@Valid @RequestBody ExhibitionEventEntity event) {
        return ResponseEntity.status(HttpStatus.CREATED).body(exhibitionEventService.createEvent(event));
    }

    @PutMapping("/events/{event_id}")
    public ResponseEntity<ExhibitionEventEntity> updateEvent(
        @PathVariable("event_id") Long eventId,
        @Valid @RequestBody ExhibitionEventEntity event
    ) {
        return ResponseEntity.ok(exhibitionEventService.updateEvent(eventId, event));
    }

    @DeleteMapping("/events/{event_id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable("event_id") Long eventId) {
        exhibitionEventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }
}
