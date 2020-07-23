package com.embevolter.logtool;

import java.util.List;

import com.embevolter.logtool.model.LogLine;

/**
 * Logtool launcher
 */
public final class Logtool {
    private Logtool() {
    }

    /**
     * Run the Logtool.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {

        if( args.length != 1 ) {
            throw new IllegalArgumentException("Invalid number of arguments! /n Logtool Usage: java -jar Logtool 'file'");
        }

        EPALogtool logtool = new EPALogtool(args[0]);

        //the EPALogtool implementation is used to read log entries into list
        List<LogLine> logLinesToWrite = logtool.readProcessor();

        //the EPALogtool implementation is used to write a list of objects into a JSON file
        logtool.writeProcessor(logLinesToWrite);
    }
}
