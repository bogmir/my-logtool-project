package com.embevolter.logtool.impl.logLineProcess;

/**
*    
*/
public interface ILogLineProcesser<T> {
   
    T processLine(String line);

}