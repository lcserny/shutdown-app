package io.github.lcserny.shutdownapp.shutdown;

import android.content.SharedPreferences;
import android.net.nsd.NsdManager;
import android.text.TextUtils;
import android.util.Log;
import io.github.lcserny.shutdownapp.ResultPair;
import io.github.lcserny.shutdownapp.ResultPairExecutor;
import io.github.lcserny.shutdownapp.UdpServerIpFinder;

import java.net.InetAddress;
import java.util.concurrent.Callable;

public class SimpleShutdownPerformer implements ShutdownPerformer {

    private static final String HOSTNAME = "winlegion";

    private final NsdManager nsdManager;
    private final SharedPreferences preferences;

    public SimpleShutdownPerformer(NsdManager nsdManager, SharedPreferences preferences) {
        this.nsdManager = nsdManager;
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
                UdpServerIpFinder ipFinder = new UdpServerIpFinder(nsdManager, preferences);
                return ipFinder.findIp(HOSTNAME);
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
