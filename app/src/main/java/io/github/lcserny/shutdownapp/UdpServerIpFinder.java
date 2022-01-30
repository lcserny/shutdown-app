package io.github.lcserny.shutdownapp;

import android.content.SharedPreferences;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import java.net.InetAddress;

public class UdpServerIpFinder {

    // TODO: remove constants and prefs if not needed
    public static final String SOCKET_TIMEOUT_KEY = "NETWORK_SCAN_SOCKET_TIMEOUT";
    public static final int DEFAULT_SOCKET_TIMEOUT = 5000;
    public static final String PROXY_PORT_KEY = "PROXY_PORT";
    public static final int DEFAULT_PROXY_PORT = 41234;

    private static final String DEVICE_ORIGIN = "ANDROID";

    private static final String SERVICE_TYPE = "_workstation._tcp.";

    private final SharedPreferences preferences;
    private final NsdManager mNsdManager;

    public InetAddress mRPiAddress;

    public UdpServerIpFinder(NsdManager mNsdManager, SharedPreferences preferences) {
        this.mNsdManager = mNsdManager;
        this.preferences = preferences;
    }

    // TODO: remove from proxy_server the UDP endpoint
    public ResultPair<InetAddress> findIp(String hostname) {
        try {
            NsdManager.ResolveListener resolveListener = initializeResolveListener();
            NsdManager.DiscoveryListener discoveryListener = initializeDiscoveryListener(hostname, resolveListener);
            mNsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, discoveryListener);

            int timeoutMsCounter = 0;
            while (timeoutMsCounter <= DEFAULT_SOCKET_TIMEOUT) {
                if (mRPiAddress == null) {
                    timeoutMsCounter += 25;
                    Thread.sleep(25);
                    continue;
                }
                return ResultPair.ResultPairBuilder.success(mRPiAddress);
            }

            return ResultPair.ResultPairBuilder.failure("Timed out trying to resolve NSD IP");
        } catch (Exception e) {
            Log.e(UdpServerIpFinder.class.getSimpleName(), e.getMessage(), e);
            return ResultPair.ResultPairBuilder.failure(e.getMessage());
        }
    }

    private NsdManager.ResolveListener initializeResolveListener() {
        return new NsdManager.ResolveListener() {
            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.e("NSD", "Resolve failed" + errorCode);
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                mRPiAddress = serviceInfo.getHost();
                Log.d("NSD", "Resolved address = " + mRPiAddress.getHostAddress());
            }
        };
    }

    private NsdManager.DiscoveryListener initializeDiscoveryListener(
            final String hostname, final NsdManager.ResolveListener resolveListener) {
        return new NsdManager.DiscoveryListener() {
            @Override
            public void onDiscoveryStarted(String regType) {
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                if (service.getServiceType().equals(SERVICE_TYPE)
                        && service.getServiceName().contains(hostname)) {
                    mNsdManager.resolveService(service, resolveListener);
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
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
}
