package com.embevolter.logtool;

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