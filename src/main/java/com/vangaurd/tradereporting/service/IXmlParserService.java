package com.vangaurd.tradereporting.service;

import com.vangaurd.tradereporting.model.Event;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

public interface IXmlParserService {

    List<Event> parseXmlFile(String directoryPath);
    Event parseSingleXmlFile(String filePath);
}
