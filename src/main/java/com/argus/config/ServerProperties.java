package com.argus.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("server")
public class ServerProperties {

    private String pairHost;
    private String pairPort;

    public String getPairHost() {
        return pairHost;
    }

    public void setPairHost(String pairHost) {
        this.pairHost = pairHost;
    }

    public String getPairPort() {
        return pairPort;
    }

    public void setPairPort(String pairPort) {
        this.pairPort = pairPort;
    }

    @Override
    public String toString() {
        return "ServerProperties{" +
                "pairHost='" + pairHost + '\'' +
                "pairPort='" + pairPort + '\'' +
                '}';
    }
}
