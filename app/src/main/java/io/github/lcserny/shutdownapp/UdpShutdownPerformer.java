package io.github.lcserny.shutdownapp;

import android.text.TextUtils;

public class UdpShutdownPerformer implements ShutdownPerformer {

    public static final String SOCKET_TIMEOUT_KEY = "NETWORK_SCAN_SOCKET_TIMEOUT";
    public static final int DEFAULT_SOCKET_TIMEOUT = 5000;
    public static final String PROXY_PORT_KEY = "PROXY_PORT";
    public static final int DEFAULT_PROXY_PORT = 41234;

    private final UdpSocketExecutor executor;

    public UdpShutdownPerformer(UdpSocketExecutor executor) {
        this.executor = executor;
    }

    @Override
    public String shutdown(String seconds) {
        if (!TextUtils.isDigitsOnly(seconds) || seconds.contains("-")) {
            seconds = "0";
        }
        String result = executor.execute("shutdown/seconds=" + seconds);
        if ("OK".equals(result.trim())) {
            return "Shutting down server";
        }
        return result;
    }
}
