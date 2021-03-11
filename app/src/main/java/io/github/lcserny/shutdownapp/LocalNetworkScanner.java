package io.github.lcserny.shutdownapp;

import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

class LocalNetworkScanner implements NetworkScanner {

    public static final String SOCKET_TIMEOUT_KEY = "NETWORK_SCAN_SOCKET_TIMEOUT";
    public static final int DEFAULT_SOCKET_TIMEOUT = 5000;

    private final WifiManager wifiManager;
    private final SharedPreferences sharedPreferences;

    LocalNetworkScanner(WifiManager wifiManager, SharedPreferences sharedPreferences) {
        this.wifiManager = wifiManager;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public List<String> scanForIPsWithListenPort(final int port) {
        int socketTimeout = sharedPreferences.getInt(SOCKET_TIMEOUT_KEY, DEFAULT_SOCKET_TIMEOUT);
        CachedThreadsExecutor executor = new CachedThreadsExecutor();
        List<String> foundHosts = new ArrayList<>();
        List<Runnable> runnableList = new ArrayList<>();

        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        int ipAddress = connectionInfo.getIpAddress();
        String ipString = Formatter.formatIpAddress(ipAddress);
        String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);

        for (int i = 0; i < 256; i++) {
            String builtAddress = prefix + i;
            runnableList.add(new NetworkScanningRunnable(builtAddress, port, foundHosts, socketTimeout));
        }
        executor.execute(runnableList, new ExecutionTimeout(10, TimeUnit.SECONDS));
        return foundHosts;
    }
}
