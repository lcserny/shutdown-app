package io.github.lcserny.shutdownapp;

import android.content.SharedPreferences;

import java.net.InetAddress;
import java.util.concurrent.*;

public class UdpFindIPExecutor {

    public static final String SOCKET_TIMEOUT_KEY = "NETWORK_SCAN_SOCKET_TIMEOUT";
    public static final int DEFAULT_SOCKET_TIMEOUT = 5000;
    public static final String PROXY_PORT_KEY = "PROXY_PORT";
    public static final int DEFAULT_PROXY_PORT = 41234;

    private final UdpClient client;
    private final SharedPreferences preferences;

    public UdpFindIPExecutor(UdpClient client, SharedPreferences preferences) {
        this.client = client;
        this.preferences = preferences;
    }

    public InetAddress execute() throws Exception {
        int socketTimeout = preferences.getInt(SOCKET_TIMEOUT_KEY, DEFAULT_SOCKET_TIMEOUT);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<ResultPair<InetAddress>> future = executor.submit(new Callable<ResultPair<InetAddress>>() {
            @Override
            public ResultPair<InetAddress> call() {
                return client.findIp();
            }
        });
        ResultPair<InetAddress> resultPair = future.get(socketTimeout, TimeUnit.MILLISECONDS);
        if (!resultPair.isSuccess()) {
            throw new RuntimeException(resultPair.getError());
        }
        return resultPair.getResult();
    }
}
