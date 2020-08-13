package com.embevolter.logtool.impl;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import com.embevolter.logtool.impl.logLineProcess.ILogLineProcesser;


/**
* Interface for LogtoolProcesser implementations
*/
public interface ILogtoolProcesser<T> {
    String filePath = "";

    List<T> readProcessor(ILogLineProcesser<T> lineProcesser);
    
    void writeProcessor(List<T> logLine, String outputFileName);
    
    Scanner initFileScanner() throws IllegalArgumentException, IOException;

    void launcher(ILogLineProcesser<T> logLineProcesser);
}
