package com.embevolter.logtool;

import com.embevolter.logtool.impl.LogtoolProcesser;
import com.embevolter.logtool.impl.logsEPA.EPALogLineProcesser;

/**
 * Logtool launcher
 * Instantiates a Logtool implementation and launches the read and write processors
 * @param args The arguments of the program.
 */
public final class Logtool {
    private Logtool() {
    }

    /**
     * @param args The one expected argument consists of the path of the input file
     */
    public static void main(String[] args) {

        //input filename is read as an argument from the command line jar
        if(args.length != 1) {
            System.out.println("Invalid number of arguments! /n Logtool Usage: java -jar Logtool 'file'");
            System.exit(0);
        }
        
        LogtoolProcesser logtool = new LogtoolProcesser(args[0]);

        logtool.launcher(new EPALogLineProcesser());
    }
}