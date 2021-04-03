package io.github.lcserny.shutdownapp;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

class NetworkScanningTask extends AsyncTask<Void, Void, List<ShutdownServer>> {

    private static final String SHUTDOWN_PATH_PATTERN = "http://%s:%s/shutdown";

    private final NetworkScanner networkScanner;
    private final int portToScan;
    private final ResultCallback<List<ShutdownServer>> resultCallback;

    NetworkScanningTask(NetworkScanner networkScanner, int portToScan, ResultCallback<List<ShutdownServer>> resultCallback) {
        this.networkScanner = networkScanner;
        this.portToScan = portToScan;
        this.resultCallback = resultCallback;
    }

    @Override
    protected List<ShutdownServer> doInBackground(Void... voids) {
        final List<String> hosts = networkScanner.scanForIPsWithListenPort(portToScan);
        List<ShutdownServer> servers = new ArrayList<>();
        for (String host : hosts) {
            servers.add(new ShutdownServer(host, String.format(SHUTDOWN_PATH_PATTERN, host, portToScan)));
        }
        return servers;
    }

    @Override
    protected void onPostExecute(List<ShutdownServer> serverList) {
        resultCallback.run(serverList);
    }
}