package io.github.lcserny.shutdownapp;

import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class ScanOnClickListener implements View.OnClickListener {

    private static final String PORT = "8076";
    private static final String SUBNET_TO_INCLUDE = "192.168";
    private static final List<String> HOSTS_TO_FIND = Collections.singletonList("winlegion");

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
        for (Map.Entry<String, String> entry : networkScanner.scanForIPsWithListenPort(PORT, SUBNET_TO_INCLUDE, HOSTS_TO_FIND).entrySet()) {
            servers.add(new ShutdownServer(entry.getValue(), String.format(SHUTDOWN_PATH_PATTERN, entry.getKey(), PORT)));
        }
        fragmentReplacer.replaceMainFragmentWith(new ServersListFragment(servers));
    }
}
