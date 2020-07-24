package com.embevolter.logtool;

import com.embevolter.logtool.impl.LogtoolProcesser;

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

        
        LogtoolProcesser logtool = new LogtoolProcesser(args[0]);

        logtool.launcher();
    }
}