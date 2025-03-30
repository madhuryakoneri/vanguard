package com.vangaurd.tradereporting.component;

import com.vangaurd.tradereporting.service.IEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@ExtendWith(OutputCaptureExtension.class)
class XMLParsingRunnerTest {

    @Mock
    private IEventService eventService;

    private static final String EVENTS_DIRECTORY_PATH = "src/test/resources/events";

    @InjectMocks
    private XMLParsingRunner xmlParsingRunner;

    @BeforeEach
    void setUp() {
        xmlParsingRunner = new XMLParsingRunner(EVENTS_DIRECTORY_PATH, eventService);
    }

    @Test
    void testRun_ShouldCallProcessXmlFile(CapturedOutput output) throws Exception {
        xmlParsingRunner.run();

        verify(eventService, times(1)).processXmlFile(EVENTS_DIRECTORY_PATH);
        assertTrue(output.getOut().contains("Starting XML Parsing..."));
        assertTrue(output.getOut().contains("XML Parsing completed."));
    }
}
