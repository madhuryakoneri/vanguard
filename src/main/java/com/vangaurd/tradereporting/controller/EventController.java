package com.vangaurd.tradereporting.controller;

import com.vangaurd.tradereporting.model.Event;
import com.vangaurd.tradereporting.service.IEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@Slf4j
public class EventController {

    private final IEventService eventService;

    public EventController(IEventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/filteredEvents")
    public ResponseEntity<List<Event>> getFilteredEvents() {
        log.info("Fetching filtered events");
        List<Event> filteredEvents = eventService.getFilteredEvents();
        log.info("Number of filtered events fetched: {}", filteredEvents.size());
        return ResponseEntity.ok(filteredEvents);
    }

}
