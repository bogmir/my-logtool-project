package com.embevolter.logtool.impl.logLineProcess;

/**
*    
*/
public interface ILogLineProcesser<T> {
   
    public T processLine(String line);

}