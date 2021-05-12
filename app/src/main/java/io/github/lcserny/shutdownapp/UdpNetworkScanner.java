package io.github.lcserny.shutdownapp;

import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import java.util.ArrayList;
import java.util.List;

import static io.github.lcserny.shutdownapp.LocalNetworkScanner.DEFAULT_SOCKET_TIMEOUT;
import static io.github.lcserny.shutdownapp.LocalNetworkScanner.SOCKET_TIMEOUT_KEY;

public class UdpNetworkScanner implements NetworkScanner {

    private final WifiManager wifiManager;
    private final SharedPreferences preferences;

    public UdpNetworkScanner(WifiManager wifiManager, SharedPreferences preferences) {
        this.wifiManager = wifiManager;
        this.preferences = preferences;
    }

    @Override
    public List<String> scanForIPsWithListenPort(int port) {
        List<String> ips = new ArrayList<>();

        CachedThreadsExecutor executor = new CachedThreadsExecutor();

        int timeout = preferences.getInt(SOCKET_TIMEOUT_KEY, DEFAULT_SOCKET_TIMEOUT);

        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        int ipAddress = connectionInfo.getIpAddress();
        String ipString = Formatter.formatIpAddress(ipAddress);
        String subnet = ipString.substring(0, ipString.lastIndexOf("."));

        // TODO: make udp client/server, send msg, check "OK" response, use timeout if nobody answers

        // TODO: share UDP client/server with shutdown performer

        return ips;
    }
}
