package com.embevolter.logtool;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.embevolter.logtool.impl.logsEPA.EPALogtoolForLine;

/**
 * Utility class
 */
public class Utils {

    public static BiFunction<Integer, Integer, Boolean> equalsOp = (a, b) -> a == b;

    public static BiFunction<Integer, Integer, Boolean> greaterThanOp = (a, b) -> a > b;

    public static BiFunction<Integer, Integer, Boolean> smallerThanOp = (a, b) -> a < b;

    public static int[] parseToIntArray(String[] arr) {
        return Stream.of(arr)
            .mapToInt(Integer::parseInt)
            .toArray();
    }
    public static String[] getArraySubset(String[] array, int startInclusive, int endExclusive) {
        return IntStream.range(startInclusive, endExclusive)
            .mapToObj(i -> array[i])
            .toArray(String[]::new);
    }

    public static String getArraySubsetToString(String[] array, int startInclusive, int endExclusive) {
        return IntStream.range(startInclusive, endExclusive)
            .mapToObj(i -> array[i])
            .collect(Collectors.joining(" "));
    }

    public static String getStringFromArray(String[] array) {
        return String.join(" ", Arrays.asList(array));
    }

    public static boolean isNullOrEmpty(final String s) {
        return s == null || s.trim().isEmpty();
    }

    public static boolean isNullOrEmpty(final Collection<?> c) {
        return c == null || c.isEmpty();
    }

    public static boolean isNullOrEmpty(final List<?> l) {
        return l == null || l.isEmpty();
    }

    public static String removeASCIIControlCharactersFromString(String text) {
        return text.replaceAll(
            EPALogtoolForLine.LineProcesserRegexEnum.ASCII_CONTROL_CHARACTERS.toString(), "");
    }

    public static String capitalize(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        }
    
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}