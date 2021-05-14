package io.github.lcserny.shutdownapp;

import android.content.SharedPreferences;

public class UdpShutdownPerformer implements ShutdownPerformer {

    public static final String SOCKET_TIMEOUT_KEY = "NETWORK_SCAN_SOCKET_TIMEOUT";
    public static final int DEFAULT_SOCKET_TIMEOUT = 5000;
    public static final String PROXY_PORT_KEY = "PROXY_PORT";
    public static final int DEFAULT_PROXY_PORT = 41234;

    private final SharedPreferences preferences;

    public UdpShutdownPerformer(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    // TODO: make udp client/server, send msg, check "OK" response, use timeout if nobody answers

    @Override
    public String shutdown(String seconds) {
        CachedThreadsExecutor executor = new CachedThreadsExecutor();
//        if (seconds.startsWith("-")) {
//            seconds = "0";
//        }
//        Request request = new Request.Builder().url(server.getShutdownUrl() + "?seconds=" + seconds).build();
//        HttpShutdownRunnable httpShutdownRunnable = new HttpShutdownRunnable(httpClient, request);
//        executor.execute(Collections.singletonList(httpShutdownRunnable), new ExecutionTimeout(10, TimeUnit.SECONDS));
//        return httpShutdownRunnable.getResponse();
        return "Works!";
    }
}
