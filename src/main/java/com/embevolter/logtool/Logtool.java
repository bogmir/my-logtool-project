package com.embevolter.logtool;

import java.util.List;

import com.embevolter.logtool.model.LogLine;

/**
 * Logtool launcher
 * Instantiates a Logtool implementation and launches the read and write processors
 * @param args The arguments of the program.
 */
public final class Logtool {
    private Logtool() {
    }

    public static void main(String[] args) {

        //input filename is read as an argument from the command line jar
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