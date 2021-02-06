package io.github.lcserny.shutdownapp;

import android.util.Log;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class LocalNetworkScanner implements NetworkScanner {

    private static final String SUBNET_TO_INCLUDE = "192.168.100";

    private final ExecutorService executorService;

    public LocalNetworkScanner(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public LocalNetworkScanner() {
        this.executorService = Executors.newCachedThreadPool();
    }

    @Override
    public List<String> scanForIPsWithListenPort(final int port) {
        final List<String> foundHosts = new ArrayList<>();

        for (int i = 0; i < 256; i++) {
            final String builtAddress = SUBNET_TO_INCLUDE + "." + i;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (LocalNetworkScanner.this.isSocketAlive(builtAddress, port)) {
                            foundHosts.add(builtAddress);
                        }
                    } catch (IOException e) {
                        Log.e(LocalNetworkScanner.class.getSimpleName(), e.getMessage());
                    }
                }
            });
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Log.e(LocalNetworkScanner.class.getSimpleName(), e.getMessage());
        }

        return foundHosts;
    }

    private boolean isSocketAlive(String hostName, int port) throws IOException {
        SocketAddress socketAddress = new InetSocketAddress(hostName, port);
        try (Socket socket = new Socket()) {
            try {
                socket.connect(socketAddress, 1000);
                return true;
            } catch (IOException e) {
                return false;
            }
        }
    }
}
