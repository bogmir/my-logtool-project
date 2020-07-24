package com.embevolter.logtool.impl;

import java.util.List;
import java.util.Scanner;

public interface ILogtoolProcesser<T> {
    String filePath = "";

    public List<?> readProcessor();
    
    public void writeProcessor(List<T> logLine);
    
    public Scanner initFileScanner() throws IllegalArgumentException;
}
