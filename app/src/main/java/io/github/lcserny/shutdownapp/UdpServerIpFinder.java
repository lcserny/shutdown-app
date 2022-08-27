package io.github.lcserny.shutdownapp;

import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.util.Log;

import net.posick.mdns.Lookup;

import org.xbill.DNS.ARecord;
import org.xbill.DNS.DClass;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

import java.net.InetAddress;

public class UdpServerIpFinder {

    // TODO: remove constants and prefs if not needed, remove them from database also
    public static final String SOCKET_TIMEOUT_KEY = "NETWORK_SCAN_SOCKET_TIMEOUT";
    public static final int DEFAULT_SOCKET_TIMEOUT = 5000;
    public static final String PROXY_PORT_KEY = "PROXY_PORT";
    public static final int DEFAULT_PROXY_PORT = 41234;

    private final SharedPreferences preferences;
    private final WifiManager wifiManager;

    public UdpServerIpFinder(WifiManager wifiManager, SharedPreferences preferences) {
        this.wifiManager = wifiManager;
        this.preferences = preferences;
    }

    // TODO: remove from proxy_server the UDP endpoint
    public ResultPair<InetAddress> findIp(String hostname) {
        try {
            InetAddress address = null;

            WifiManager.MulticastLock lock = wifiManager.createMulticastLock("mDNS-Lock");
            lock.setReferenceCounted(true);
            lock.acquire();
            for (int i = 0; i < 10; i++) {
                Lookup lookup = new Lookup(hostname, Type.A, DClass.IN);
                Record[] records = lookup.lookupRecords();
                if (records != null && records.length >= 1) {
                    ARecord aRecord = (ARecord) records[0];
                    address = aRecord.getAddress();
                    break;
                }
            }
            lock.release();

            if (address != null) {
                return ResultPair.ResultPairBuilder.success(address);
            }
            return ResultPair.ResultPairBuilder.failure("Timed out trying to resolve NSD IP");
        } catch (Exception e) {
            Log.e(UdpServerIpFinder.class.getSimpleName(), e.getMessage(), e);
            return ResultPair.ResultPairBuilder.failure(e.getMessage());
        }
    }
}
