package com.embevolter.logtool;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.embevolter.logtool.model.LogLine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for line processing in Logtool App.
 */
class LogtoolTest implements TestLifecycleLogger {
    EPALogtool logtool = null;

    BiFunction<Integer, Integer, Boolean> equalsOp = (a,b) -> a == b;
    BiFunction<Integer, Integer, Boolean> greaterThanOp = (a,b) -> a > b;
    BiFunction<Integer, Integer, Boolean> smallerThanOp = (a,b) -> a < b;

    //host, datetime{dd,hh,mm,ss}, requestMethod, urlPath, protocol/protocolVersion, httpAnswerCode, requestSize
    static final int NUMBER_OF_PROPERTIES_PER_LINE = 7;

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

    @BeforeEach
    void beforeEach() {
        logtool = new EPALogtool("resources/epa-http.txt");
    }

    
    /**
     * Total number of lines read should be 47,748
     */
    @Test
    void totalNumberOfLoadedLines() {
        try (Scanner sc = logtool.initFileScanner()) {
            int counter = 0;
            while (sc.hasNextLine()) {
                sc.nextLine();
                counter++;
                //Line l = logUtility.readLine(line);
            }

            assertEquals(47748, counter);
        }
    }
    

    @Test
    void testReadAllLines() {
        try {
            logtool.readProcessor();
        } catch (Exception ex) {
            fail(" READING NOT COMPLETE ");
        }

        logger.info("The read was successful");
    }


    List<LogLine> readSubsetOfLogLinesWithEqualNormalParams(int numberOfPropertiesReadPerLine) {
        return readSubsetOfLogLines(numberOfPropertiesReadPerLine, equalsOp);
    }

    List<LogLine> readSubsetOfLogLinesWithLessThanNormalParams(int numberOfPropertiesReadPerLine) {
        return readSubsetOfLogLines(numberOfPropertiesReadPerLine, smallerThanOp);
    }

    List<LogLine> readSubsetOfLogLinesWithMoreThanNormalParams(int numberOfPropertiesReadPerLine) {
        return readSubsetOfLogLines(numberOfPropertiesReadPerLine, greaterThanOp);
    }

    List<LogLine> readSubsetOfLogLines(int numberOfPropertiesReadPerLine, BiFunction<Integer, Integer, Boolean> operatorFunction) {
        List<LogLine> logLinesToWrite = new ArrayList<LogLine>();

        int counter = 0;
        try (Scanner sc = logtool.initFileScanner()) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                int lineParametersLength = line.split(" ").length;
                if (operatorFunction.apply(lineParametersLength, numberOfPropertiesReadPerLine)){
                    counter++;
                    LogLine l = logtool.readLine(line);
                    logLinesToWrite.add(l);
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
        logger.info(String.format("A total of %s lines were processed", counter));
        return logLinesToWrite;
    }

    @Test
    void testWriteLinesWhenNormalFormat() {
        List<LogLine> logLinesToWrite = null;

        try {
            logLinesToWrite = readSubsetOfLogLinesWithEqualNormalParams(NUMBER_OF_PROPERTIES_PER_LINE);
        } catch (Exception ex) {
            fail("The reading of normal lines was not possible!");
        }

        try {
            logtool.writeProcessor(logLinesToWrite);
        } catch (Exception ex) {
            fail("The writing of normal lines was not possible!");
        }

        logger.info("The writing of normal lines was successful");
    }

    @Test
    void testWriteLinesWhenLessParamsFormat() {
        List<LogLine> logLinesToWrite = null;

        try {
            logLinesToWrite = readSubsetOfLogLinesWithLessThanNormalParams(NUMBER_OF_PROPERTIES_PER_LINE);
        } catch (Exception ex) {
            fail("The reading of normal lines was not possible!");
        }

        try {
            logtool.writeProcessor(logLinesToWrite);
        } catch (Exception ex) {
            fail("The writing of normal lines was not possible!");
        }

        logger.info("The writing of normal lines was successful");
    }

    @Test
    void testWriteLinesWhenMoreParamsFormat() {
        List<LogLine> logLinesToWrite = null;

        try {
            logLinesToWrite = readSubsetOfLogLinesWithMoreThanNormalParams(NUMBER_OF_PROPERTIES_PER_LINE);
        } catch (Exception ex) {
            fail("The reading of normal lines was not possible!");
        }

        try {
            logtool.writeProcessor(logLinesToWrite);
        } catch (Exception ex) {
            fail("The writing of normal lines was not possible!");
        }

        logger.info("The writing of normal lines was successful");
    }


    @Test
    void testReadLinesWhenNormalFormat() {
        try {
            readSubsetOfLogLinesWithEqualNormalParams(NUMBER_OF_PROPERTIES_PER_LINE);
        } catch (Exception ex) {
            fail("The reading of normal lines was not possible!");
        }

        logger.info("The read was successful");
    }


    @Test
    void allLinesContainHost() {
        try (Scanner sc = logtool.initFileScanner()) {
            //String hostNameRegex = "\\b((?=[a-z0-9-]{1,63}\\.)(xn--)?[a-z0-9]+(-[a-z0-9]+)*\\.)+[a-z]{2,63}\\b";
            String validIpAddressRegex = "(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])";
            String validHostnameRegex = "(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9])";
            Pattern patternValidIpAddressRegex = Pattern.compile(validIpAddressRegex);
            Pattern patternValidHostnameRegex = Pattern.compile(validHostnameRegex);

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                Matcher matcherValidIpAddressRegex = patternValidIpAddressRegex.matcher(line);
                Matcher matcherValidHostnameRegex = patternValidHostnameRegex.matcher(line);
                if (!matcherValidHostnameRegex.find() && !matcherValidIpAddressRegex.find()) {
                    fail("No host in log line!");
                }
            }
        }

        logger.info("All lines contain a host");
    }

    @Test
    void allLinesContainDatetime() {
        testMatchingRegexOnEachLine("\\[\\d{2}:\\d{2}:\\d{2}:\\d{2}\\]", "No [dd:hh:mm:ss] in log line!", "All lines contain datetime");
    }

    @Disabled("faulty input file must be dealt with")
    @Test
    void allLinesContainProtocol() {
        testMatchingRegexOnEachLine("[A-Z]+/\\d+\\.\\d", "No Protocol in log line!", "All lines contain a Protocol");
    }

    @Disabled("faulty input file must be dealt with")
    @Test
    void allLinesContainRequestMethod() {
        testMatchingRegexOnEachLine("\"[A-Z]+", "No Request Method in log line!", "All lines contain a Request Method");
    }

    @Test
    void allLinesContainResponseSize() {
        testMatchingRegexOnEachLine("\\s[\\d-]+$", "No Response Size in log line!", "All lines contain Response Size");
    }

    @Test
    void allLinesContainResponseStatus() {
        testMatchingRegexOnEachLine("[\\d]+\\s[\\d-]+$", "No Response Status in log line!", "All lines contain Response Status");
    }

    @Test
    void allLinesContainURLPath() {
        testMatchingRegexOnEachLine("\"[A..Z]*\\s[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]*", "No URL PATH in log line!", "All lines contain URL PATH");
    }

    @Test
    void equalsGetRequests() {
        int counter = 0;
        try (Scanner sc = logtool.initFileScanner()) {
            String validGetRegex = "\"GET";
            Pattern patternValidGetRegex = Pattern.compile(validGetRegex);

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                Matcher matcherValidGetRegex = patternValidGetRegex.matcher(line);
                if (matcherValidGetRegex.find()) {
                    counter++;
                }
            }
        }
        logger.info("GET requests" + counter);
        assertEquals(counter, 46014);
    }

    @Test
    void equalsInvalidRequests() {
        int counter = 0;
        try (Scanner sc = logtool.initFileScanner()) {
            String validGetRegex = "\"\\s400";
            Pattern patternValidGetRegex = Pattern.compile(validGetRegex);

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                Matcher matcherValidGetRegex = patternValidGetRegex.matcher(line);
                if (matcherValidGetRegex.find()) {
                    counter++;
                }
            }
        }
        logger.info("Invalid requests: " + counter);
        assertEquals(counter, 6);
    }

    @Test
    void readsFirstLineIntoObject() {
        try (Scanner sc = logtool.initFileScanner()) {
            String line = sc.nextLine();
            assertNotNull(logtool.readLine(line), "Bravo!");
        }
    }
}
