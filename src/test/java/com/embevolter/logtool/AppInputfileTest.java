package com.embevolter.logtool;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.embevolter.logtool.impl.LogtoolProcesser;
import com.embevolter.logtool.impl.logsEPA.EPALogtoolForLine;
import com.embevolter.logtool.model.LogLine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the input file in the Logtool App.
 */
public class AppInputfileTest implements TestLifecycleLogger {
    LogtoolProcesser<LogLine> logtool;

    static final String inputFileName = "resources/epa-http.txt";

    static final int NUMBER_OF_GET_REQUESTS = 46014;
    static final int NUMBER_OF_HEAD_REQUESTS = 106;
    static final int NUMBER_OF_LOG_LINES = 47748;
    static final int NUMBER_OF_POST_REQUESTS = 1622;
    static final int NUMBER_OF_PROPERTIES_PER_LINE = 7;
    static final int NUMBER_OF_INVALID_REQUESTS = 6;

    List<LogLine> loglines;


    @BeforeEach
    void beforeEach() {
        logtool = new LogtoolProcesser<LogLine>(inputFileName);
    }

    private int someLinesContainOrDoNotContainMatchingSequence(String regex, boolean doContain) {
        int counter = 0;
        try (Scanner sc = logtool.initFileScanner()) {
            Pattern pattern = Pattern.compile(regex);

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                Matcher matcher = pattern.matcher(line);
                if (doContain && matcher.find() ||     //when CONTAINS method is tested
                    !doContain && !matcher.find()) {   //when DOES NOT CONTAIN method is tested

                    counter++;
                }
            }
        }

        return counter;
    }

    private int someLinesContainMatchingSequence(String regex) {
        return someLinesContainOrDoNotContainMatchingSequence(regex, true);
    }

    private int someLinesDoNotContainMatchingSequence(String regex) {
        return someLinesContainOrDoNotContainMatchingSequence(regex, false);
    }

    private void testMatchingRegexOnEachLine(String regex, String messageFail, String messagePass) {
        try (Scanner sc = logtool.initFileScanner()) {
            Pattern pattern = Pattern.compile(regex);

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                Matcher matcher = pattern.matcher(line);
                if (!matcher.find()) {
                    logger.info(line);
                    logger.warning(messageFail);
                    fail(messageFail);
                }
            }
        }

        logger.info(messagePass);
    }

    @Test
    void allLinesContainHost() {
        try (Scanner sc = logtool.initFileScanner()) {
            Pattern patternValidIpAddressRegex = Pattern.compile(
                EPALogtoolForLine.RegexEnum.IP_ADDRESS.toString());
            Pattern patternValidHostnameRegex = Pattern.compile(
                EPALogtoolForLine.RegexEnum.HOST.toString());

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                Matcher matcherValidIpAddressRegex = 
                    patternValidIpAddressRegex.matcher(line);
                Matcher matcherValidHostnameRegex = 
                    patternValidHostnameRegex.matcher(line);

                if (!matcherValidHostnameRegex.find() && !matcherValidIpAddressRegex.find()) {
                    fail("No host in log line!");
                }
            }
        }

        logger.info("All lines contain a host");
    }

    @Test
    void allLinesContainDatetime() {
        testMatchingRegexOnEachLine(EPALogtoolForLine.RegexEnum.DATETIME.toString(),
            "No [dd:hh:mm:ss] in log line!", "All lines contain datetime");
    }

    @Disabled("faulty input file must be dealt with")
    @Test
    void allLinesContainProtocol() {
        testMatchingRegexOnEachLine(EPALogtoolForLine.RegexEnum.PROTOCOL.toString(),
             "No Protocol in log line!", "All lines contain a Protocol");
    }

    @Test
    void someLinesDoNotContainProtocol() {
        int counter = 
            someLinesDoNotContainMatchingSequence(EPALogtoolForLine.RegexEnum.PROTOCOL.toString());
        
        logger.info("Number of lines that do not contain protocol:" + counter);
        assertNotEquals(counter, 0);
    }

    @Test
    void someLinesDoNotContainRequestMethod() {
        int counter = 
            someLinesDoNotContainMatchingSequence(EPALogtoolForLine.RegexEnum.REQUEST_METHOD.toString());
        
        logger.info("Number of lines that do not contain request method:" + counter);
        assertNotEquals(counter, 0);
    }

    @Disabled("faulty input file must be dealt with")
    @Test
    void allLinesContainRequestMethod() {
        testMatchingRegexOnEachLine(EPALogtoolForLine.RegexEnum.REQUEST_METHOD.toString(),
            "No Request Method in log line!", "All lines contain a Request Method");
    }

    @Test
    void allLinesContainResponseSize() {
        testMatchingRegexOnEachLine(EPALogtoolForLine.RegexEnum.RESPONSE_SIZE.toString(),
             "No Response Size in log line!", "All lines contain Response Size");
    }

    @Test
    void allLinesContainResponseStatus() {
        testMatchingRegexOnEachLine(EPALogtoolForLine.RegexEnum.RESPONSE_STATUS.toString(),
            "No Response Status in log line!", "All lines contain Response Status");
    }

    @Test
    void allLinesContainURLPath() {
        testMatchingRegexOnEachLine(EPALogtoolForLine.RegexEnum.URL_PATH.toString(), 
            "No URL PATH in log line!", "All lines contain URL PATH");
    }

    @Test
    void someLinesContainASCIIControlCharacters() {
        int counter = 
            someLinesContainMatchingSequence(EPALogtoolForLine.RegexEnum.ASCII_CONTROL_CHARACTERS.toString());

        logger.info(
            String.format("ASCII control characters in [%s] lines", counter));

        assertNotEquals(counter, 0);
    }

    @Test
    void someLinesContainGetRequests() {
        int counter = 
            someLinesContainMatchingSequence(EPALogtoolForLine.RegexEnum.GET_REQUEST.toString());

        logger.info("GET requests:" + counter);
        assertEquals(counter, NUMBER_OF_GET_REQUESTS);
    }

    @Test
    void someLinesContainPostRequests() {
        int counter = 
            someLinesContainMatchingSequence(EPALogtoolForLine.RegexEnum.POST_REQUEST.toString());

        logger.info("POST requests:" + counter);
        assertEquals(counter, NUMBER_OF_POST_REQUESTS);
    }

    @Test
    void someLinesContainHeadRequests() {
        int counter = 
            someLinesContainMatchingSequence(EPALogtoolForLine.RegexEnum.HEAD_REQUEST.toString());

        logger.info("HEAD requests:" + counter);
        assertEquals(counter, NUMBER_OF_HEAD_REQUESTS);
    }


    @Test
    void someLinesContainInvalidRequests() {
        int counter = 
            someLinesContainMatchingSequence(EPALogtoolForLine.RegexEnum.REQUEST_INVALID.toString());
        
        logger.info("Invalid requests: " + counter);
        assertEquals(counter, NUMBER_OF_INVALID_REQUESTS);
    }

    @Test
    void readsFirstLineIntoObject() {
        EPALogtoolForLine logtoolForLine = new EPALogtoolForLine();
        try (Scanner sc = logtool.initFileScanner()) {
            String line = sc.nextLine();
            assertNotNull(logtoolForLine.readLine(line), "Bravo!");
        }
    }

    @Test
    void totalNumberOfReadLines() {
        try (Scanner sc = logtool.initFileScanner()) {
            int counter = 0;
            while (sc.hasNextLine()) {
                sc.nextLine();
                counter++;
                //Line l = logUtility.readLine(line);
            }

            assertEquals(NUMBER_OF_LOG_LINES, counter);
        }
    }

}
