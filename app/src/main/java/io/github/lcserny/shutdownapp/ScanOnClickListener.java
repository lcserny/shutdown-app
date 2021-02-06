package io.github.lcserny.shutdownapp;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

class ScanOnClickListener implements View.OnClickListener {

    private static final int PORT = 8077;

    private static final String SHUTDOWN_PATH_PATTERN = "http://%s:%s/shutdown"; // TODO

    private final MainFragmentReplacer fragmentReplacer;
    private final NetworkScanner networkScanner;

    public ScanOnClickListener(MainFragmentReplacer fragmentReplacer, NetworkScanner networkScanner) {
        this.fragmentReplacer = fragmentReplacer;
        this.networkScanner = networkScanner;
    }

    @Override
    public void onClick(View v) {
        List<ShutdownServer> servers = new ArrayList<>();
        // TODO: loading icon in UI?
        for (String host : networkScanner.scanForIPsWithListenPort(PORT)) {
            servers.add(new ShutdownServer(host, String.format(SHUTDOWN_PATH_PATTERN, host, PORT)));
        }
        fragmentReplacer.replaceMainFragmentWith(new ServersListFragment(servers));
    }
}