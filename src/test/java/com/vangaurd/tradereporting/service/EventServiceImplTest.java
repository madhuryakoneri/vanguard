package com.vangaurd.tradereporting.service;

import com.vangaurd.tradereporting.model.Event;
import com.vangaurd.tradereporting.repository.EventRepository;
import com.vangaurd.tradereporting.service.impl.EventServiceImpl;
import com.vangaurd.tradereporting.utils.AnagramUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private IXmlParserService xmlParserService;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<Event> criteriaQuery;

    @Mock
    private Root<Event> root;

    @Mock
    private TypedQuery<Event> typedQuery;

    @InjectMocks
    private EventServiceImpl eventService;

    @BeforeEach
    void setUp() {
        lenient().when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        lenient().when(criteriaBuilder.createQuery(Event.class)).thenReturn(criteriaQuery);
        lenient().when(criteriaQuery.from(Event.class)).thenReturn(root);
        lenient().when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
        lenient().when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
    }

    @Test
    void processXmlFile_shouldParseAndSaveEvents() {
        String filePath = "src/test/resources/";
        List<Event> mockEvents = Arrays.asList(new Event(), new Event());

        when(xmlParserService.parseXmlFile(any())).thenReturn(mockEvents);

        eventService.processXmlFile(filePath);

        verify(xmlParserService, times(1)).parseXmlFile(filePath);
        verify(eventRepository, times(1)).saveAll(mockEvents);
    }

    @Test
    void processXmlFile_shouldHandleExceptionGracefully() {
        String filePath = "src/test/resources/";
        when(xmlParserService.parseXmlFile(filePath)).thenThrow(new RuntimeException("Parse error"));

        eventService.processXmlFile(filePath);

        verify(xmlParserService, times(1)).parseXmlFile(filePath);
        verify(eventRepository, never()).saveAll(any());
    }

    @Test
    void getFilteredEvents_shouldReturnFilteredEvents() {
        Event event1 = new Event();
        event1.setSellerParty("EMU_BANK");
        event1.setPremiumCurrency("AUD");
        event1.setBuyerParty("ANKB_MEU"); // Anagram of EMU_BANK

        Event event2 = new Event();
        event2.setSellerParty("BISON_BANK");
        event2.setPremiumCurrency("USD");
        event2.setBuyerParty("BISON_BANK"); // Not an anagram

        List<Event> mockEvents = Arrays.asList(event1, event2);

        when(typedQuery.getResultList()).thenReturn(mockEvents);
        mockStatic(AnagramUtil.class);
        when(AnagramUtil.isAnagram("EMU_BANK", "ANKB_MEU")).thenReturn(true);
        when(AnagramUtil.isAnagram("BISON_BANK", "BISON_BANK")).thenReturn(false);

        List<Event> filteredEvents = eventService.getFilteredEvents();

        assertEquals(1, filteredEvents.size());
        assertEquals("BISON_BANK", filteredEvents.get(0).getSellerParty());

        verify(entityManager, times(1)).createQuery(criteriaQuery);
        verify(typedQuery, times(1)).getResultList();
    }

    @Test
    void getFilteredEvents_shouldReturnEmptyListIfNoMatches() {
        when(typedQuery.getResultList()).thenReturn(Collections.emptyList());

        List<Event> filteredEvents = eventService.getFilteredEvents();

        assertTrue(filteredEvents.isEmpty());

        verify(entityManager, times(1)).createQuery(criteriaQuery);
        verify(typedQuery, times(1)).getResultList();
    }
}
