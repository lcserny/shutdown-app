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
    private final LogEntryDAO logEntryDAO;

    LocalNetworkScanner(WifiManager wifiManager, SharedPreferences sharedPreferences, LogEntryDAO logEntryDAO) {
        this.wifiManager = wifiManager;
        this.sharedPreferences = sharedPreferences;
        this.logEntryDAO = logEntryDAO;
    }

    @Override
    public List<String> scanForIPsWithListenPort(final int port) {
        List<LogEntry> logEntries = new ArrayList<>();

        long start = System.currentTimeMillis();
        int socketTimeout = sharedPreferences.getInt(SOCKET_TIMEOUT_KEY, DEFAULT_SOCKET_TIMEOUT);
        logEntries.add(LogEntryConverter.convertForTimeTook("get socketTimeout from SharedPrefs", start));

        start = System.currentTimeMillis();
        CachedThreadsExecutor executor = new CachedThreadsExecutor();
        logEntries.add(LogEntryConverter.convertForTimeTook("create a new CachedThreadExecutor", start));

        List<String> foundHosts = new ArrayList<>();
        List<Runnable> runnableList = new ArrayList<>();

        start = System.currentTimeMillis();
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        logEntries.add(LogEntryConverter.convertForTimeTook("get Wifi connection info", start));

        start = System.currentTimeMillis();
        int ipAddress = connectionInfo.getIpAddress();
        logEntries.add(LogEntryConverter.convertForTimeTook("get ipAddress from Wifi connection info", start));

        start = System.currentTimeMillis();
        String ipString = Formatter.formatIpAddress(ipAddress);
        logEntries.add(LogEntryConverter.convertForTimeTook("get format ipAddress of connection info", start));

        String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);

        for (int i = 0; i < 256; i++) {
            String builtAddress = prefix + i;
            runnableList.add(new NetworkScanningRunnable(builtAddress, port, foundHosts, socketTimeout, logEntryDAO));
        }

        start = System.currentTimeMillis();
        executor.execute(runnableList, new ExecutionTimeout(10, TimeUnit.SECONDS));
        logEntries.add(LogEntryConverter.convertForTimeTook("execute network scanning pings", start));

        logEntryDAO.insert(logEntries);

        return foundHosts;
    }
}
