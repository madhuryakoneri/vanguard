package com.vangaurd.tradereporting.service;

import com.vangaurd.tradereporting.model.Event;
import com.vangaurd.tradereporting.service.impl.XmlParserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;


import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class XmlParserServiceImplTest {

    @Autowired
    private ResourceLoader resourceLoader;

    @InjectMocks
    private XmlParserServiceImpl xmlParserServiceImpl;

    @BeforeEach
    void setUp() {
        xmlParserServiceImpl = new XmlParserServiceImpl(resourceLoader);
    }

    @Test
    public void testParseXmlFile_ValidFile() throws Exception {
        String filePath = "classpath:events";
        List<Event> result = xmlParserServiceImpl.parseXmlFile(filePath);
        assertEquals(1, result.size());
        assertEquals("EMU_BANK", result.get(0).getSellerParty());
        assertEquals("AUD", result.get(0).getPremiumCurrency());
        assertEquals("LEFT_BANK", result.get(0).getBuyerParty());
        assertEquals(new BigDecimal("100.00"), result.get(0).getPremiumAmount());
    }

    @Test
    public void testParseXmlFile_InvalidFile() {
        String filePath = "classpath:event";
        List<Event> result = xmlParserServiceImpl.parseXmlFile(filePath);
        assertEquals(0, result.size());
    }

    @Test
    public void testParseSingleXmlFile_InvalidFile(){
        String filePath = "path/to/invalid/file.xml";
        Event event = xmlParserServiceImpl.parseSingleXmlFile(filePath);
        assertThat(event, hasProperty("id", nullValue()));
        assertThat(event, hasProperty("buyerParty", nullValue()));
        assertThat(event, hasProperty("sellerParty", nullValue()));
        assertThat(event, hasProperty("premiumAmount", nullValue()));
        assertThat(event, hasProperty("premiumCurrency", nullValue()));
    }
}