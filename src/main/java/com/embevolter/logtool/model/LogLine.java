package com.embevolter.logtool.model;

public class LogLine {
    //private String regularExpression; 
    private String host;
    private Datetime datetime;
    private ServerRequest serverRequest;
    private String httpAnswerCode;
    private String requestSize;

   
    public LogLine(String host, Datetime datetime, ServerRequest serverRequest,
            String httpAnswerCode, String requestSize) {
        this.host = host;
        this.datetime = datetime;
        this.serverRequest = serverRequest;
        this.httpAnswerCode = httpAnswerCode;
        this.requestSize = requestSize;
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Datetime getDatetime() {
        return datetime;
    }

    public void setDatetime(Datetime datetime) {
        this.datetime = datetime;
    }

    public ServerRequest getServerRequest() {
        return serverRequest;
    }

    public void setServerRequest(ServerRequest serverRequest) {
        this.serverRequest = serverRequest;
    }

    public String getHttpAnswerCode() {
        return httpAnswerCode;
    }

    public void setHttpAnswerCode(String httpAnswerCode) {
        this.httpAnswerCode = httpAnswerCode;
    }

    public String getRequestSize() {
        return requestSize;
    }

    public void setRequestSize(String requestSize) {
        this.requestSize = requestSize;
    }

    @Override
    public String toString() {
        return "Line [datetime=" + datetime + ", requestSize=" + requestSize + ", host=" + host
                + ", httpAnswerCode=" + httpAnswerCode + ", serverRequest="
                + serverRequest + "]";
    }
    
    
}