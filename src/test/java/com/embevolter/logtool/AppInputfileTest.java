package com.embevolter.logtool;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for parameter setup in Logtool App.
 */
public class AppInputfileTest {
    @Test
    void runFiletoolWithNullInputParameter () {
        Logtool.main(null);
        //assertThrows(IllegalArgumentException.class, "");

    }
}