package io.github.lcserny.shutdownapp;

class ShutdownServer {

    private final String ip;
    private final String shutdownUrl;

    public ShutdownServer(String ip, String shutdownUrl) {
        this.ip = ip;
        this.shutdownUrl = shutdownUrl;
    }

    public String getIp() {
        return ip;
    }

    public String getShutdownUrl() {
        return shutdownUrl;
    }
}
