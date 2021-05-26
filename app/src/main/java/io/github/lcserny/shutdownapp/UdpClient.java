package io.github.lcserny.shutdownapp;

import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.AndroidRuntimeException;
import android.util.Log;
import io.github.lcserny.shutdownapp.ResultPair.ResultPairBuilder;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static io.github.lcserny.shutdownapp.UdpSocketExecutor.*;

class UdpClient {

    private static final String SOCKET_ERROR = "UDP SocketException exception occurred";

    private final WifiManager wifiManager;
    private final SharedPreferences preferences;

    public UdpClient(WifiManager wifiManager, SharedPreferences preferences) {
        this.wifiManager = wifiManager;
        this.preferences = preferences;
    }

    String execute(String payload) {
        DatagramSocket socket = null;
        try {
            InetAddress broadcastAddress = InetAddress.getByName(getSubnetAddress());

            int proxyPort = preferences.getInt(PROXY_PORT_KEY, DEFAULT_PROXY_PORT);

            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            byte[] sendData = payload.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcastAddress, proxyPort);

            socket = createSocket();
            socket.send(sendPacket);
            socket.receive(receivePacket);

            return new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
        } catch (Exception e) {
            Log.e(UdpClient.class.getSimpleName(), SOCKET_ERROR, e);
            return e.getMessage();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

    private DatagramSocket createSocket() throws IOException {
        int socketTimeout = preferences.getInt(SOCKET_TIMEOUT_KEY, DEFAULT_SOCKET_TIMEOUT);
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);
        socket.setSoTimeout(socketTimeout);
        return socket;
    }

    private String getSubnetAddress() {
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        int ipAddress = connectionInfo.getIpAddress();
        String ipString = Formatter.formatIpAddress(ipAddress);
        String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
        return prefix + "255";
    }
}
