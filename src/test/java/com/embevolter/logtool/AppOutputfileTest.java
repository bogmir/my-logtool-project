package com.embevolter.logtool;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import com.embevolter.logtool.impl.LogtoolProcesser;
import com.embevolter.logtool.model.LogLine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

/**
 * Unit tests for the output of the Logtool App.
 * 
 * The Jackson JSON parser is a well-known&reliable JAVA library, i.e. the
 * process of writing an ArrayList<Object> into a JSON file is assumed correct.
 * 
 * These tests are performed exclusively on the output dataaset that is obtained
 * as a result of processing the initial log file, and subsequently serialized
 * to a JSON file with a JSON parser.
 * 
 */
public class AppOutputfileTest implements TestLifecycleLogger {
    LogtoolProcesser<LogLine> logtool = null;

    static final String inputFileName = "resources/epa-http.txt";
    static final int NUMBER_OF_LOG_LINES = 47748;
    

    List<LogLine> loglines;

    @BeforeEach
    void beforeEach() {
        logtool = new LogtoolProcesser<LogLine>(inputFileName);

        /*
         * read&process all lines of the input file into a result list before each test
         */
        this.loglines = (List<LogLine>) logtool.readProcessor();
    }

    public boolean containsPropertyWithValue(final List<LogLine> list, final String propertyName, final String value) {
        Method getMethod = getPropertyGetterMethod(propertyName);

        return list.stream().anyMatch(o -> {
            try {
                return (getMethod.invoke(o)).equals(value);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }

            return false;
        });
    }

    public boolean containsPropertyWithEmptyValue(final List<LogLine> list, final String propertyName) {
        return containsPropertyWithValue(list, propertyName, "");
    }

    public Method getPropertyGetterMethod(final String propertyName) {
        Method getMethod;
        try {
            getMethod = LogLine.class.getMethod("get" + Utils.capitalize(propertyName));
        } catch (NoSuchMethodException ex) {
            return null;
        }

        return getMethod;
    }

    @Test
    void allLinesContainHost() {
        assertFalse(
            loglines.stream()
            .anyMatch(o -> Utils.isNullOrEmpty(o.getHost()))
        );
    }

    @Disabled("Not the case for this input file")
    @Test
    void thereAreLinesWithEmptyHost() {
        LogLine logLineWithEmptyHost = loglines.stream()
            .filter(o -> "".equals(o.getHost()))
            .findAny().orElse(null);
        
        assertNotNull(logLineWithEmptyHost);
    }

    @Test
    void allLinesContainDatetime() {
        assertFalse(
            loglines.stream()
                .anyMatch(o -> (o.getDatetime() == null) 
                    || Utils.isNullOrEmpty(o.getDatetime().toString()))
        );
    }

    @Disabled("Not the case for this input file")
    @Test
    void thereAreLinesWithEmptyDatetime() {
        LogLine logLineWithEmptyHost = loglines.stream()
            .filter(o -> "".equals(o.getDatetime().toString()))
            .findAny().orElse(null);
        
        assertNotNull(logLineWithEmptyHost);
    }

    @Disabled("Not the case for this input file")
    @Test
    void allLinesContainURL() {
        assertFalse(
            loglines.stream()
                .anyMatch(o -> (o.getServerRequest() == null) 
                    || Utils.isNullOrEmpty(o.getServerRequest().getUrl()))
            );
    }

    //@Disabled("Not the case for this input file")
    @Test
    void thereAreLinesWithEmptyURL() {
        List<LogLine> logLinesWithEmptyURL = loglines.stream()
            .filter(o -> (o.getServerRequest() == null) 
                || Utils.isNullOrEmpty(o.getServerRequest().getUrl()))
            .collect(Collectors.toList());

        logger.info(" Number of objects with empty Request Method: " + logLinesWithEmptyURL.size());   

        assertFalse( Utils.isNullOrEmpty(logLinesWithEmptyURL));
    }

    @Disabled("Not the case for this input file")
    @Test
    void allLinesContainRequestMethod() {
        assertFalse(
            loglines.stream()
                .anyMatch(o -> o.getServerRequest() == null 
                    || Utils.isNullOrEmpty(o.getServerRequest().getHttpRequestMethod()))
            );
    }

    @Test
    void thereAreLinesWithEmptyRequestMethod() {
        List<LogLine> logLinesWithEmptyRequestMethod = loglines.stream()
            .filter(o -> (o.getServerRequest() == null) 
                || Utils.isNullOrEmpty(o.getServerRequest().getHttpRequestMethod()))
            .collect(Collectors.toList());

        logger.info(" Number of objects with empty Request Method: " + logLinesWithEmptyRequestMethod.size());   

        assertFalse( Utils.isNullOrEmpty(logLinesWithEmptyRequestMethod));
    }

    @Disabled("Not the case for this input file")
    @Test
    void allLinesContainRequestProtocol() {
        assertFalse(
            loglines.stream()
                .anyMatch(o -> o.getServerRequest() == null 
                    || Utils.isNullOrEmpty(o.getServerRequest().getProtocol()))
            );
    }

    //@Disabled("Not the case for this input file")
    @Test
    void thereAreLinesWithEmptyProtocol() {
        List<LogLine> logLinesWithEmptyProtocol = loglines.stream()
            .filter(o -> (o.getServerRequest() == null) 
                || Utils.isNullOrEmpty(o.getServerRequest().getProtocol()))
            .collect(Collectors.toList());

        logger.info(" Number of objects with empty Protocol: " + logLinesWithEmptyProtocol.size());   

        assertFalse( Utils.isNullOrEmpty(logLinesWithEmptyProtocol) );
    }

    @Test
    void allLinesContainAnswerCode() {
        assertFalse(
            loglines.stream()
            .anyMatch(o -> Utils.isNullOrEmpty(o.getHttpAnswerCode()))
        );
    }

    @Disabled("Not the case for this input file")
    @Test
    void thereAreLinesWithEmptyHttpAnswerCode() {
        LogLine logLineWithEmptyHttpAnswerCode = loglines.stream()
            .filter(o -> "".equals(o.getHttpAnswerCode()))
            .findAny().orElse(null);
        
        assertNotNull(logLineWithEmptyHttpAnswerCode);
    }

    @Test
    void allLinesContainRequestSize() {
        assertFalse(
            loglines.stream()
            .anyMatch(o -> Utils.isNullOrEmpty(o.getRequestSize()))
        );
    }

    @Disabled("Not the case for this input file")
    @Test
    void thereAreLinesWithEmptyRequestSize() {
        LogLine logLineWithEmptyRequestSize = loglines.stream()
            .filter(o -> "".equals(o.getRequestSize()))
            .findAny().orElse(null);
        
        assertNotNull(logLineWithEmptyRequestSize);
    }
    
    
    @Test
    void totalNumberOfLinesWrittenInOutput() {
        assertEquals(loglines.size(), NUMBER_OF_LOG_LINES);
    }

    @Test
    void thereAreNoASCIIControlCharactersInOutput() {
        assertFalse(loglines.stream()
            .anyMatch(o -> StringUtils.containsIsoControlCharacter(o.toString()))
        );
    }
}
