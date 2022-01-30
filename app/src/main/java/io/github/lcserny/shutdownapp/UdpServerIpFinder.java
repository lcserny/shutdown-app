package io.github.lcserny.shutdownapp;

import android.content.SharedPreferences;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.WifiInfo;
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

    private final SharedPreferences preferences;




    // Network Service Discovery related members
    // This allows the app to discover the garagedoor.local
    // "service" on the local network.
    // Reference: http://developer.android.com/training/connect-devices-wirelessly/nsd.html
    private NsdManager mNsdManager;
    private NsdManager.DiscoveryListener mDiscoveryListener;
    private NsdManager.ResolveListener mResolveListener;
    private NsdServiceInfo mServiceInfo;

    public InetAddress mRPiAddress;

    // The NSD service type that the RPi exposes.
    private static final String SERVICE_TYPE = "_workstation._tcp.";

    public UdpServerIpFinder(NsdManager mNsdManager, SharedPreferences preferences) {
        this.mNsdManager = mNsdManager;
        this.preferences = preferences;
    }






    public ResultPair<InetAddress> findIp(String hostname) {
//        try (DatagramSocket socket = createSocket()) {
        try {
            initializeResolveListener();
            initializeDiscoveryListener(hostname);
            mNsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);



//            byte[] sendData = DEVICE_ORIGIN.getBytes();
//            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
//                    InetAddress.getByName(getSubnetAddress()), preferences.getInt(PROXY_PORT_KEY, DEFAULT_PROXY_PORT));
//            socket.send(sendPacket);
//
//            byte[] receiveData = new byte[1024];
//            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//            socket.receive(receivePacket);

            // TODO: loop for timeout until address is set?
            return ResultPair.ResultPairBuilder.success(mRPiAddress);
        } catch (Exception e) {
            Log.e(UdpServerIpFinder.class.getSimpleName(), e.getMessage(), e);
            return ResultPair.ResultPairBuilder.failure(e.getMessage());
        }
    }

    private void initializeDiscoveryListener(final String hostname) {
        // Instantiate a new DiscoveryListener
        mDiscoveryListener = new NsdManager.DiscoveryListener() {

            //  Called as soon as service discovery begins.
            @Override
            public void onDiscoveryStarted(String regType) {
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                // A service was found!  Do something with it.
                String name = service.getServiceName();
                String type = service.getServiceType();
                Log.d("NSD", "Service Name=" + name);
                Log.d("NSD", "Service Type=" + type);
                if (type.equals(SERVICE_TYPE) && name.contains(hostname)) {
                    Log.d("NSD", "Service Found @ '" + name + "'");
                    mNsdManager.resolveService(service, mResolveListener);
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                // When the network service is no longer available.
                // Internal bookkeeping code goes here.
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                mNsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                mNsdManager.stopServiceDiscovery(this);
            }
        };
    }

    private void initializeResolveListener() {
        mResolveListener = new NsdManager.ResolveListener() {

            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Called when the resolve fails.  Use the error code to debug.
                Log.e("NSD", "Resolve failed" + errorCode);
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                mServiceInfo = serviceInfo;

                // Port is being returned as 9. Not needed.
                //int port = mServiceInfo.getPort();

                InetAddress host = mServiceInfo.getHost();
                String address = host.getHostAddress();
                Log.d("NSD", "Resolved address = " + address);
                mRPiAddress = host;
            }
        };
    }

//    private DatagramSocket createSocket() throws IOException {
//        DatagramSocket socket = new DatagramSocket();
//        socket.setBroadcast(true);
//        socket.setSoTimeout(preferences.getInt(SOCKET_TIMEOUT_KEY, DEFAULT_SOCKET_TIMEOUT));
//        return socket;
//    }
//
//    private String getSubnetAddress() {
//        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
//        int ipAddress = connectionInfo.getIpAddress();
//        String ipString = Formatter.formatIpAddress(ipAddress);
//        String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
//        return prefix + "255";
//    }
}
