package io.github.lcserny.shutdownapp;

import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.*;

// TODO: refactor: this, UdpSocketCallable and UdpClient
public class UdpShutdownPerformer implements ShutdownPerformer {

    public static final String SOCKET_TIMEOUT_KEY = "NETWORK_SCAN_SOCKET_TIMEOUT";
    public static final int DEFAULT_SOCKET_TIMEOUT = 5000;
    public static final String PROXY_PORT_KEY = "PROXY_PORT";
    public static final int DEFAULT_PROXY_PORT = 41234;

    private final SharedPreferences preferences;
    private final WifiManager wifiManager;

    public UdpShutdownPerformer(SharedPreferences preferences, WifiManager wifiManager) {
        this.preferences = preferences;
        this.wifiManager = wifiManager;
    }

    @Override
    public String shutdown(String seconds) {
        int socketTimeout = preferences.getInt(SOCKET_TIMEOUT_KEY, DEFAULT_SOCKET_TIMEOUT);
        int proxyPort = preferences.getInt(PROXY_PORT_KEY, DEFAULT_PROXY_PORT);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        if (!TextUtils.isDigitsOnly(seconds) || seconds.contains("-")) {
            seconds = "0";
        }
        String payload = "shutdown/seconds=" + seconds;

        UdpSocketCallable runnable = new UdpSocketCallable(new UdpClient(getSubnetAddress(), proxyPort), payload);
        Future<String> futureResult = executor.submit(runnable);
        try {
            String result = futureResult.get(socketTimeout, TimeUnit.MILLISECONDS);
            if ("".equals(result.trim())) {
                return "Shutting down server";
            }
            return result;
        } catch (Exception e) {
            Log.e(UdpShutdownPerformer.class.getSimpleName(), "Error occurred executing udp shutdown", e);
            return e.getMessage();
        }
    }

    private String getSubnetAddress() {
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        int ipAddress = connectionInfo.getIpAddress();
        String ipString = Formatter.formatIpAddress(ipAddress);
        String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
        return prefix + "255";
    }
}
