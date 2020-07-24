package com.embevolter.logtool.impl;

import java.util.List;
import java.util.Scanner;

public interface LogtoolInterface<T> {
    String filePath = "";

    public List<T> readProcessor();
    
    public void writeProcessor(List<T> logLine);
    
    public Scanner initFileScanner() throws IllegalArgumentException;
}
