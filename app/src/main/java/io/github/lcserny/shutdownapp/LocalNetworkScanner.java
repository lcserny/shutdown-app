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

    @Override
    public List<String> scanForIPsWithListenPort(final int port) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final List<String> foundHosts = new ArrayList<>();

        for (int i = 0; i < 256; i++) {
            final String builtAddress = SUBNET_TO_INCLUDE + "." + i;
            executorService.execute(new Runnable() {
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
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            Log.e(LocalNetworkScanner.class.getSimpleName(), e.getMessage());
        }

        return foundHosts;
    }

    private boolean isSocketAlive(String hostName, int port) throws IOException {
        SocketAddress socketAddress = new InetSocketAddress(hostName, port);
        try (Socket socket = new Socket()) {
            try {
                socket.connect(socketAddress, 3000);
                return true;
            } catch (IOException e) {
                return false;
            }
        }
    }
}
