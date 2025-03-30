package com.vangaurd.tradereporting.controller;

import com.vangaurd.tradereporting.model.Event;
import com.vangaurd.tradereporting.service.impl.EventServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
public class EventControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventServiceImpl eventService;

    @Test
    void testGetFilteredEvents() throws Exception {
        when(eventService.getFilteredEvents()).thenReturn(Collections.singletonList(new Event()));

        mockMvc.perform(get("/api/events/filteredEvents")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}