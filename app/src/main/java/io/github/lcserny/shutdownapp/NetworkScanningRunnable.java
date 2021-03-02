package io.github.lcserny.shutdownapp;

import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;

class NetworkScanningRunnable implements Runnable {

    private static final int SOCKET_TIMEOUT = 3000;

    private final String address;
    private final int port;
    private final List<String> foundHosts;

    NetworkScanningRunnable(String address, int port, List<String> foundHosts) {
        this.address = address;
        this.port = port;
        this.foundHosts = foundHosts;
    }

    @Override
    public void run() {
        try {
            if (isSocketAlive(address, port)) {
                foundHosts.add(address);
            }
        } catch (IOException e) {
            Log.e(NetworkScanningRunnable.class.getSimpleName(), e.getMessage());
        }
    }

    private boolean isSocketAlive(String hostName, int port) throws IOException {
        SocketAddress socketAddress = new InetSocketAddress(hostName, port);
        try (Socket socket = new Socket()) {
            try {
                socket.connect(socketAddress, SOCKET_TIMEOUT);
                return true;
            } catch (IOException e) {
                return false;
            }
        }
    }
}
