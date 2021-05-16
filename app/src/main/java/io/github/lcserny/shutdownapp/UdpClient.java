package io.github.lcserny.shutdownapp;

import android.util.Log;

import java.io.IOException;
import java.net.*;

class UdpClient {

    private static final String SOCKET_ERROR = "UDP SocketException execption occurred";
    private final String subnetAddress;
    private final int port;

    public UdpClient(String subnetAddress, int port) {
        this.subnetAddress = subnetAddress;
        this.port = port;
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
            broadcastAddress = InetAddress.getByName(subnetAddress);
        } catch (UnknownHostException e) {
            Log.e(UdpClient.class.getSimpleName(), SOCKET_ERROR, e);
            return SOCKET_ERROR;
        }

        String result;
        DatagramPacket packet = new DatagramPacket(packetData, packetData.length, broadcastAddress, port);
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
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            socket.setBroadcast(true);
        } catch (SocketException e) {
            Log.e(UdpClient.class.getSimpleName(), SOCKET_ERROR, e);
        }
        return socket;
    }
}
