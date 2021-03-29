package io.github.lcserny.shutdownapp;

import android.content.SharedPreferences;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

class CachedLocalNetworkScanner extends LocalNetworkScanner {

    private static final String CACHED_SERVERS_KEY = "CACHED_SERVERS";

    CachedLocalNetworkScanner(WifiManager wifiManager, SharedPreferences sharedPreferences, LogPersistenceEnabledDaoWrapper logEntryDAO) {
        super(wifiManager, sharedPreferences, logEntryDAO);
    }

    @Override
    public List<String> scanForIPsWithListenPort(final int port) {
        Set<String> cachedHosts = sharedPreferences.getStringSet(CACHED_SERVERS_KEY, new HashSet<String>());
        if (areNotReachable(cachedHosts, port)) {
            List<String> foundHosts = super.scanForIPsWithListenPort(port);
            saveKnownHosts(foundHosts);
            return foundHosts;
        }
        return new ArrayList<>(cachedHosts);
    }

    private boolean areNotReachable(Set<String> cachedHosts, int port) {
        if (cachedHosts.isEmpty()) {
            return true;
        }

        int socketTimeout = sharedPreferences.getInt(SOCKET_TIMEOUT_KEY, DEFAULT_SOCKET_TIMEOUT);

        List<String> reachedHosts = new ArrayList<>();
        List<LogEntry> logEntries = new ArrayList<>();
        List<Runnable> runnableList = new ArrayList<>();

        CachedThreadsExecutor executor = new CachedThreadsExecutor();
        for (String cachedHost : cachedHosts) {
            runnableList.add(new NetworkScanningRunnable(cachedHost, port, reachedHosts, socketTimeout, logEntries));
        }
        executor.execute(runnableList, new ExecutionTimeout(10, TimeUnit.SECONDS));

        return cachedHosts.size() != reachedHosts.size();
    }

    private void saveKnownHosts(List<String> foundHosts) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(CACHED_SERVERS_KEY, new HashSet<>(foundHosts));
        editor.apply();
    }
}
