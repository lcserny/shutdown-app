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

public class UdpServerIpFinder {

    public static final String SOCKET_TIMEOUT_KEY = "NETWORK_SCAN_SOCKET_TIMEOUT";
    public static final int DEFAULT_SOCKET_TIMEOUT = 5000;
    public static final String PROXY_PORT_KEY = "PROXY_PORT";
    public static final int DEFAULT_PROXY_PORT = 41234;

    private static final String DEVICE_ORIGIN = "ANDROID";

    private final WifiManager wifiManager;
    private final SharedPreferences preferences;

    public UdpServerIpFinder(WifiManager wifiManager, SharedPreferences preferences) {
        this.wifiManager = wifiManager;
        this.preferences = preferences;
    }

    public ResultPair<InetAddress> findIp() {
        try (DatagramSocket socket = createSocket()) {
            byte[] sendData = DEVICE_ORIGIN.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                    InetAddress.getByName(getSubnetAddress()), preferences.getInt(PROXY_PORT_KEY, DEFAULT_PROXY_PORT));
            socket.send(sendPacket);

            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            return ResultPair.ResultPairBuilder.success(receivePacket.getAddress());
        } catch (Exception e) {
            Log.e(UdpServerIpFinder.class.getSimpleName(), e.getMessage(), e);
            return ResultPair.ResultPairBuilder.failure(e.getMessage());
        }
    }

    private DatagramSocket createSocket() throws IOException {
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);
        socket.setSoTimeout(preferences.getInt(SOCKET_TIMEOUT_KEY, DEFAULT_SOCKET_TIMEOUT));
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
