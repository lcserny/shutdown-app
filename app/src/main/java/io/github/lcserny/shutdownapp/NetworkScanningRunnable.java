package io.github.lcserny.shutdownapp;

import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;

class NetworkScanningRunnable implements Runnable {

    private final String address;
    private final int port;
    private final List<String> foundHosts;
    private final int socketTimeout;
    private final List<LogEntry> logEntries;

    NetworkScanningRunnable(String address, int port, List<String> foundHosts, int socketTimeout, List<LogEntry> logEntries) {
        this.address = address;
        this.port = port;
        this.foundHosts = foundHosts;
        this.socketTimeout = socketTimeout;
        this.logEntries = logEntries;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        try {
            if (isSocketAlive(address, port, socketTimeout)) {
                foundHosts.add(address);
            }
        } catch (IOException e) {
            Log.e(NetworkScanningRunnable.class.getSimpleName(), e.getMessage());
        }
        logEntries.add(LogEntryConverter.convertForTimeTook("ping address " + address, start));
    }

    private boolean isSocketAlive(String hostName, int port, int socketTimeout) throws IOException {
        SocketAddress socketAddress = new InetSocketAddress(hostName, port);
        try (Socket socket = new Socket()) {
            try {
                socket.connect(socketAddress, socketTimeout);
                return true;
            } catch (IOException e) {
                return false;
            }
        }
    }
}
