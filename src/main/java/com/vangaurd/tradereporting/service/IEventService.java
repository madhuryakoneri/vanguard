package com.vangaurd.tradereporting.service;

import com.vangaurd.tradereporting.model.Event;
import org.springframework.core.io.Resource;

import java.util.List;

public interface IEventService {
    void processXmlFile(String filePath) throws Exception;
    List<Event> getFilteredEvents();
}
