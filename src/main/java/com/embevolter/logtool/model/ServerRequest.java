package com.embevolter.logtool.model;

import com.embevolter.logtool.Utils;

public class ServerRequest {
    private String httpRequestMethod;
    private String url;
    private String protocol;
    private String protocolVersion;

    public ServerRequest(String httpRequestMethod, String url, String protocol, String protocolVersion) {
        this.httpRequestMethod = httpRequestMethod;
        this.url = url;
        this.protocol = protocol;
        this.protocolVersion = protocolVersion;
    }

    public String getHttpRequestMethod() {
        return httpRequestMethod;
    }

    public void setHttpRequestMethod(String httpRequestMethod) {
        this.httpRequestMethod = httpRequestMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    @Override
    public String toString() {
        if (Utils.isNullOrEmpty(httpRequestMethod) && Utils.isNullOrEmpty(url) 
            && Utils.isNullOrEmpty(protocol) && Utils.isNullOrEmpty(protocolVersion)) {
            return "";
        }

        return "ServerRequest [httpRequestMethod=" + httpRequestMethod + ", protocol=" + protocol + ", protocolVersion="
                + protocolVersion + ", url=" + url + "]";
    }
    
}