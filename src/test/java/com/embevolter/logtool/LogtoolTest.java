package com.embevolter.logtool;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.BiFunction;

import com.embevolter.logtool.impl.LogtoolProcesser;
import com.embevolter.logtool.impl.logsEPA.EPALogtoolForLine;
import com.embevolter.logtool.model.LogLine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for line processing in Logtool App.
 */
class LogtoolTest implements TestLifecycleLogger {
    LogtoolProcesser<LogLine> logtool;
    EPALogtoolForLine logtoolForLine;

    static final String inputFileName = "resources/epa-http.txt";
    static final int NUMBER_OF_LOG_LINES = 47748;
    static final int NUMBER_OF_PROPERTIES_PER_LINE = 7;


    @BeforeEach
    void beforeEach() {
        logtool = new LogtoolProcesser<LogLine>(inputFileName);
        logtoolForLine = new EPALogtoolForLine();
    }

    private List<LogLine> readSubsetOfLogLines(int numberOfPropertiesReadPerLine, BiFunction<Integer, Integer, Boolean> operatorFunction) {
        List<LogLine> logLinesToWrite = new ArrayList<LogLine>();

        int counter = 0;
        try (Scanner sc = logtool.initFileScanner()) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                int lineParametersLength = line.split(" ").length;
                if (operatorFunction.apply(lineParametersLength, numberOfPropertiesReadPerLine)){
                    counter++;
                    LogLine l = logtoolForLine.readLine(line);
                    logLinesToWrite.add(l);
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
        logger.info(String.format("A total of %s lines were processed", counter));
        return logLinesToWrite;
    }

    private List<LogLine> readSubsetOfLogLinesWithEqualNormalParams(int numberOfPropertiesReadPerLine) {
        return readSubsetOfLogLines(numberOfPropertiesReadPerLine, Utils.equalsOp);
    }

    private List<LogLine> readSubsetOfLogLinesWithLessThanNormalParams(int numberOfPropertiesReadPerLine) {
        return readSubsetOfLogLines(numberOfPropertiesReadPerLine, Utils.smallerThanOp);
    }

    private List<LogLine> readSubsetOfLogLinesWithMoreThanNormalParams(int numberOfPropertiesReadPerLine) {
        return readSubsetOfLogLines(numberOfPropertiesReadPerLine, Utils.greaterThanOp);
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


    @Test
    void testWriteLinesWhenNormalFormat() {
        List<LogLine> logLinesToWrite = null;

        try {
            logLinesToWrite = readSubsetOfLogLinesWithEqualNormalParams(NUMBER_OF_PROPERTIES_PER_LINE);
        } catch (Exception ex) {
            fail("The reading of normal lines was not possible!");
        }

        try {
            logtool.writeProcessor(logLinesToWrite, "resources/test/epa-http-test-normallinesonly.json");
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
            logtool.writeProcessor(logLinesToWrite, "resources/test/epa-http-test-shorterlinesonly.json");
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
            logtool.writeProcessor(logLinesToWrite, "resources/test/epa-http-test-largerlinesonly.json");
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

        logger.info("The read was successful!");
    }

}