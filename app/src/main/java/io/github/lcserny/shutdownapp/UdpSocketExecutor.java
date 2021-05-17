package io.github.lcserny.shutdownapp;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.concurrent.*;

class UdpSocketExecutor {

    public static final String SOCKET_TIMEOUT_KEY = "NETWORK_SCAN_SOCKET_TIMEOUT";
    public static final int DEFAULT_SOCKET_TIMEOUT = 5000;
    public static final String PROXY_PORT_KEY = "PROXY_PORT";
    public static final int DEFAULT_PROXY_PORT = 41234;

    private final UdpClient client;
    private final SharedPreferences preferences;

    UdpSocketExecutor(UdpClient client, SharedPreferences preferences) {
        this.client = client;
        this.preferences = preferences;
    }

    String execute(final String payload) {
        int socketTimeout = preferences.getInt(SOCKET_TIMEOUT_KEY, DEFAULT_SOCKET_TIMEOUT);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return client.execute(payload);
            }
        });
        try {
            return future.get(socketTimeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            Log.e(UdpShutdownPerformer.class.getSimpleName(), "Error occurred executing udp socket command", e);
            return e.getMessage();
        }
    }
}
