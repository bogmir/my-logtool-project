package com.embevolter.logtool.impl;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import com.embevolter.logtool.impl.logsEPA.ILogLineProcesser;


/**
* Interface for LogtoolProcesser implementations
*/
public interface ILogtoolProcesser<T> {
    String filePath = "";

    public List<?> readProcessor(ILogLineProcesser<T> lineProcesser);
    
    public void writeProcessor(List<T> logLine, String outputFileName);
    
    public Scanner initFileScanner() throws IllegalArgumentException, IOException;
}
