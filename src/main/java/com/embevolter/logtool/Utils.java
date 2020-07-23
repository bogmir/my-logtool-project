package com.embevolter.logtool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Utils {
    static int[] parseToIntArray(String[] arr) {
        return Stream.of(arr)
            .mapToInt(Integer::parseInt)
            .toArray();
    }
    static String[] getArraySubset(String[] array, int startInclusive, int endExclusive) {
        return IntStream.range(startInclusive, endExclusive)
            .mapToObj(i -> array[i])
            .toArray(String[]::new);
    }

    static String getArraySubsetToString(String[] array, int startInclusive, int endExclusive) {
        return IntStream.range(startInclusive, endExclusive)
            .mapToObj(i -> array[i])
            .collect(Collectors.joining(" "));
    }

    static String removeASCIIControlCharactersFromString(String text) {
        //TODO: refactor?
        Pattern patternASCIIControlCharacter = Pattern.compile("[\\p{Cntrl}&&[^\r\n\t]]");
        Matcher matcher = patternASCIIControlCharacter.matcher(text);
        if (matcher.find()) {
            text.replaceAll("[^\\x00-\\x7F]", "");
        }
        
        return text;
    }
}