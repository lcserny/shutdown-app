package io.github.lcserny.shutdownapp;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

class ScanOnClickListener implements View.OnClickListener {

    private static final String SHUTDOWN_PATH_PATTERN = "http://%s:%s/shutdown";

    private final MainFragmentReplacer fragmentReplacer;
    private final NetworkScanner networkScanner;
    private final int portToScan;

    public ScanOnClickListener(MainFragmentReplacer fragmentReplacer, NetworkScanner networkScanner, String portToScan) {
        this.fragmentReplacer = fragmentReplacer;
        this.networkScanner = networkScanner;
        this.portToScan = Integer.parseInt(portToScan);
    }

    @Override
    public void onClick(View v) {
        List<ShutdownServer> servers = new ArrayList<>();
        // TODO: loading icon in UI?
        for (String host : networkScanner.scanForIPsWithListenPort(portToScan)) {
            servers.add(new ShutdownServer(host, String.format(SHUTDOWN_PATH_PATTERN, host, portToScan)));
        }
        fragmentReplacer.replaceMainFragmentWith(new ServersListFragment(servers));
    }
}
