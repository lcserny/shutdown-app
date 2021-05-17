package io.github.lcserny.shutdownapp;

import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

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
        byte[] receiveBuffer = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        DatagramSocket socket = createSocket();
        if (socket == null) {
            return SOCKET_ERROR;
        }

        byte[] packetData = payload.getBytes();
        InetAddress broadcastAddress;
        try {
            broadcastAddress = InetAddress.getByName(getSubnetAddress());
        } catch (UnknownHostException e) {
            Log.e(UdpClient.class.getSimpleName(), SOCKET_ERROR, e);
            return SOCKET_ERROR;
        }

        int proxyPort = preferences.getInt(PROXY_PORT_KEY, DEFAULT_PROXY_PORT);
        String result;
        DatagramPacket packet = new DatagramPacket(packetData, packetData.length, broadcastAddress, proxyPort);
        try {
            socket.send(packet);
            socket.receive(receivePacket);
            result = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
        } catch (IOException e) {
            Log.e(UdpClient.class.getSimpleName(), SOCKET_ERROR, e);
            result = SOCKET_ERROR;
        }
        socket.close();
        return result;
    }

    private DatagramSocket createSocket() {
        int socketTimeout = preferences.getInt(SOCKET_TIMEOUT_KEY, DEFAULT_SOCKET_TIMEOUT);
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            socket.setBroadcast(true);
            socket.setSoTimeout(socketTimeout);
        } catch (Exception e) {
            Log.e(UdpClient.class.getSimpleName(), SOCKET_ERROR, e);
        }
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
