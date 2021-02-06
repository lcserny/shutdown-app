package io.github.lcserny.shutdownapp;

import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class LocalNetworkScanner implements NetworkScanner {

    @Override
    public Map<String, String> scanForIPsWithListenPort(String port, final String subnetToInclude, final List<String> hostsToFind) {
        final Map<String, String> foundHosts = new HashMap<>();

        try {
            ExecutorService executorService = Executors.newCachedThreadPool();
            // TODO: allow requests in Android
            final Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()) {
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        NetworkInterface networkInterface = networkInterfaces.nextElement();
                        Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();

                        while (addresses.hasMoreElements()) {
                            InetAddress address = addresses.nextElement();
                            String hostName = address.getHostName();
                            String hostAddress = address.getHostAddress();

                            if (hostsToFind.contains(hostName) && hostAddress.startsWith(subnetToInclude)) {
                                foundHosts.put(hostName, hostAddress);
                            }
                        }
                    }
                });
            }

            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            Log.e(LocalNetworkScanner.class.getSimpleName(), e.getMessage());
        }

        return foundHosts;
    }
}
