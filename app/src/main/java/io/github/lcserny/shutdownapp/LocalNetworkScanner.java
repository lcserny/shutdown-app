package io.github.lcserny.shutdownapp;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

class LocalNetworkScanner implements NetworkScanner {

    private final WifiManager wifiManager;

    LocalNetworkScanner(WifiManager wifiManager) {
        this.wifiManager = wifiManager;
    }

    @Override
    public List<String> scanForIPsWithListenPort(final int port) {
        CachedThreadsExecutor executor = new CachedThreadsExecutor();
        List<String> foundHosts = new ArrayList<>();
        List<Runnable> runnableList = new ArrayList<>();

        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        int ipAddress = connectionInfo.getIpAddress();
        String ipString = Formatter.formatIpAddress(ipAddress);
        String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);

        for (int i = 0; i < 256; i++) {
            String builtAddress = prefix + i;
            runnableList.add(new NetworkScanningRunnable(builtAddress, port, foundHosts));
        }
        executor.execute(runnableList, new ExecutionTimeout(10, TimeUnit.SECONDS));
        return foundHosts;
    }
}
