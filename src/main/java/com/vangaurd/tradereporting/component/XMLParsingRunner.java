package com.vangaurd.tradereporting.component;

import com.vangaurd.tradereporting.service.IEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class XMLParsingRunner implements CommandLineRunner {

    @Value("${events.directory}")
    private final String eventsDirectoryPath;

    private final IEventService eventService;

    @Autowired
    public XMLParsingRunner(@Value("${events.directory}") String eventsDirectoryPath, IEventService eventService) {
        this.eventsDirectoryPath = eventsDirectoryPath;
        this.eventService = eventService;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting XML Parsing...");
        eventService.processXmlFile(eventsDirectoryPath);
        log.info("XML Parsing completed.");
    }
}
