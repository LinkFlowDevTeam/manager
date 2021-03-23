package com.test32.common.model;

public class InitializeServerConfigRequest
{
    private String serverType;
    private String config;

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}