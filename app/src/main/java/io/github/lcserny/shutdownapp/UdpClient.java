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

import static io.github.lcserny.shutdownapp.UdpSocketExecutor.*;

class UdpClient {

    private final WifiManager wifiManager;
    private final SharedPreferences preferences;

    public UdpClient(WifiManager wifiManager, SharedPreferences preferences) {
        this.wifiManager = wifiManager;
        this.preferences = preferences;
    }

    String execute(String payload) {
        try (DatagramSocket socket = createSocket()) {
            byte[] sendData = payload.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                    InetAddress.getByName(getSubnetAddress()), preferences.getInt(PROXY_PORT_KEY, DEFAULT_PROXY_PORT));
            socket.send(sendPacket);

            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            return new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
        } catch (Exception e) {
            Log.e(UdpClient.class.getSimpleName(), e.getMessage(), e);
            return e.getMessage();
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
