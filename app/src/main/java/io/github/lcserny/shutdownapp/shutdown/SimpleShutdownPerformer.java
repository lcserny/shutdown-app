package io.github.lcserny.shutdownapp.shutdown;

import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;
import io.github.lcserny.shutdownapp.ResultPair;
import io.github.lcserny.shutdownapp.ResultPairExecutor;
import io.github.lcserny.shutdownapp.UdpServerIpFinder;

import java.net.InetAddress;
import java.util.concurrent.Callable;

public class SimpleShutdownPerformer implements ShutdownPerformer {

    private final WifiManager wifiManager;
    private final SharedPreferences preferences;

    public SimpleShutdownPerformer(WifiManager wifiManager, SharedPreferences preferences) {
        this.wifiManager = wifiManager;
        this.preferences = preferences;
    }

    @Override
    public String shutdown(String seconds) {
        if (!TextUtils.isDigitsOnly(seconds) || seconds.contains("-")) {
            seconds = "0";
        }

        try {
            String ip = findIpInAnotherThread();
            shutdownFromAnotherThread(ip, seconds);
            return "Shutting down server";
        } catch (Exception e) {
            Log.e(SimpleShutdownPerformer.class.getSimpleName(), e.getMessage(), e);
            return e.getMessage();
        }
    }

    private String findIpInAnotherThread() throws Exception {
        ResultPairExecutor<InetAddress> findIpExecutor = new ResultPairExecutor<>();
        InetAddress address = findIpExecutor.execute(new Callable<ResultPair<InetAddress>>() {
            @Override
            public ResultPair<InetAddress> call() throws Exception {
                UdpServerIpFinder ipFinder = new UdpServerIpFinder(wifiManager, preferences);
                return ipFinder.findIp();
            }
        });
        return address.getHostAddress();
    }

    private void shutdownFromAnotherThread(final String ip, final String seconds) throws Exception {
        ResultPairExecutor<Void> shutdownExecutor = new ResultPairExecutor<>();
        shutdownExecutor.execute(new Callable<ResultPair<Void>>() {
            @Override
            public ResultPair<Void> call() throws Exception {
                HttpShutdownClient client = new HttpShutdownClient(preferences);
                return client.sendShutdown(ip, seconds);
            }
        });
    }
}
