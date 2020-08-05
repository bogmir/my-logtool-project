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
import com.embevolter.logtool.impl.logLineProcess.EPALogLineProcesser;
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

    private int numberOfLinesMatchingSequenceOrNot(String regex, boolean doContain) {
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

    private int numberOfLinesMatchingSequence(String regex) {
        return numberOfLinesMatchingSequenceOrNot(regex, true);
    }

    private int numberOfLinesNotMatchingSequence(String regex) {
        return numberOfLinesMatchingSequenceOrNot(regex, false);
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
                EPALogLineProcesser.LineProcesserRegexEnum.IP_ADDRESS.toString());
            Pattern patternValidHostnameRegex = Pattern.compile(
                EPALogLineProcesser.LineProcesserRegexEnum.HOST.toString());

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
        testMatchingRegexOnEachLine(EPALogLineProcesser.LineProcesserRegexEnum.DATETIME.toString(),
            "No [dd:hh:mm:ss] in log line!", "All lines contain datetime");
    }

    @Disabled("faulty input file must be dealt with")
    @Test
    void allLinesContainProtocol() {
        testMatchingRegexOnEachLine(EPALogLineProcesser.LineProcesserRegexEnum.PROTOCOL.toString(),
             "No Protocol in log line!", "All lines contain a Protocol");
    }

    @Test
    void someLinesDoNotContainProtocol() {
        int counter = 
            numberOfLinesNotMatchingSequence(EPALogLineProcesser.LineProcesserRegexEnum.PROTOCOL.toString());
        
        logger.info("Number of lines that do not contain protocol:" + counter);
        assertNotEquals(counter, 0);
    }

    @Test
    void someLinesDoNotContainRequestMethod() {
        int counter = 
            numberOfLinesNotMatchingSequence(EPALogLineProcesser.LineProcesserRegexEnum.REQUEST_METHOD.toString());
        
        logger.info("Number of lines that do not contain request method:" + counter);
        assertNotEquals(counter, 0);
    }

    @Disabled("faulty input file must be dealt with")
    @Test
    void allLinesContainRequestMethod() {
        testMatchingRegexOnEachLine(EPALogLineProcesser.LineProcesserRegexEnum.REQUEST_METHOD.toString(),
            "No Request Method in log line!", "All lines contain a Request Method");
    }

    @Test
    void allLinesContainResponseSize() {
        testMatchingRegexOnEachLine(EPALogLineProcesser.LineProcesserRegexEnum.RESPONSE_SIZE.toString(),
             "No Response Size in log line!", "All lines contain Response Size");
    }

    @Test
    void allLinesContainResponseStatus() {
        testMatchingRegexOnEachLine(EPALogLineProcesser.LineProcesserRegexEnum.RESPONSE_STATUS.toString(),
            "No Response Status in log line!", "All lines contain Response Status");
    }

    @Test
    void allLinesContainURLPath() {
        testMatchingRegexOnEachLine(EPALogLineProcesser.LineProcesserRegexEnum.URL_PATH.toString(), 
            "No URL PATH in log line!", "All lines contain URL PATH");
    }

    @Test
    void someLinesContainASCIIControlCharacters() {
        int counter = numberOfLinesMatchingSequence("\\p{Cc}");

        logger.info(
            String.format("ASCII control characters in [%s] lines", counter));

        assertNotEquals(counter, 0);
    }

    @Test
    void someLinesContainGetRequests() {
        int counter = 
            numberOfLinesMatchingSequence(EPALogLineProcesser.LineProcesserRegexEnum.GET_REQUEST.toString());

        logger.info("GET requests:" + counter);
        assertEquals(counter, NUMBER_OF_GET_REQUESTS);
    }

    @Test
    void someLinesContainPostRequests() {
        int counter = 
            numberOfLinesMatchingSequence(EPALogLineProcesser.LineProcesserRegexEnum.POST_REQUEST.toString());

        logger.info("POST requests:" + counter);
        assertEquals(counter, NUMBER_OF_POST_REQUESTS);
    }

    @Test
    void someLinesContainHeadRequests() {
        int counter = 
            numberOfLinesMatchingSequence(EPALogLineProcesser.LineProcesserRegexEnum.HEAD_REQUEST.toString());

        logger.info("HEAD requests:" + counter);
        assertEquals(counter, NUMBER_OF_HEAD_REQUESTS);
    }


    @Test
    void someLinesContainInvalidRequests() {
        int counter = 
            numberOfLinesMatchingSequence(EPALogLineProcesser.LineProcesserRegexEnum.REQUEST_INVALID.toString());
        
        logger.info("Invalid requests: " + counter);
        assertEquals(counter, NUMBER_OF_INVALID_REQUESTS);
    }

    @Test
    void readsFirstLineIntoObject() {
        EPALogLineProcesser logtoolForLine = new EPALogLineProcesser();
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
