package com.vangaurd.tradereporting.service.impl;

import com.vangaurd.tradereporting.model.Event;
import com.vangaurd.tradereporting.service.IXmlParserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class XmlParserServiceImpl implements IXmlParserService {

    private final ResourceLoader resourceLoader;

    @Override
    public List<Event> parseXmlFile(String directoryPath) {
        List<Event> events = new ArrayList<>();
        Resource eventsDirectory = resourceLoader.getResource(directoryPath);
        File directory = null;
        try {
            directory = eventsDirectory.getFile();
        if (directory.isDirectory()) {
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                log.info("Processing file: {}", file.getName());
                if (file.isFile() && file.getName().endsWith(".xml")) {
                    events.add(parseSingleXmlFile(file.getAbsolutePath()));
                }
            }
        } else {
            log.error("Invalid directory path: {}", directoryPath);
        }
    } catch (IOException e) {
        log.error("Exception occurred while accessing directory path: {}", directoryPath, e);
    }
        return events;
    }

    @Override
    public Event parseSingleXmlFile(String filePath) {
        Event event = new Event();
        try {
            File file = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();

            XPathExpression buyerExpr = xpath.compile("//buyerPartyReference/@href");
            XPathExpression sellerExpr = xpath.compile("//sellerPartyReference/@href");
            XPathExpression amountExpr = xpath.compile("//paymentAmount/amount");
            XPathExpression currencyExpr = xpath.compile("//paymentAmount/currency");

            event.setBuyerParty((String) buyerExpr.evaluate(doc, XPathConstants.STRING));
            event.setSellerParty((String) sellerExpr.evaluate(doc, XPathConstants.STRING));
            event.setPremiumAmount(new BigDecimal((String) amountExpr.evaluate(doc, XPathConstants.STRING)));
            event.setPremiumCurrency((String) currencyExpr.evaluate(doc, XPathConstants.STRING));

            log.debug("Parsed File: {}, Event: {}", filePath, event);
            return event;
        } catch (Exception e) {
            log.error("Error parsing file: {}", filePath, e);
        }
        return event;
    }
}
