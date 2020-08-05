package com.embevolter.logtool.impl.logsEPA;

/**
*    
*/
public interface ILogLineProcesser<T> {
   
    public T processLine(String line);

}