package io.github.lcserny.shutdownapp;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

class ScanOnClickListener implements View.OnClickListener {

    private static final String SHUTDOWN_PATH = ":8076/shutdown";

    private final MainFragmentReplacer fragmentReplacer;

    public ScanOnClickListener(MainFragmentReplacer fragmentReplacer) {
        this.fragmentReplacer = fragmentReplacer;
    }

    @Override
    public void onClick(View v) {
        // TODO: scan for IPs, when done replace fragment
        List<ShutdownServer> servers = new ArrayList<>();
        servers.add(new ShutdownServer("1.1.1.1", "http://something"));
        servers.add(new ShutdownServer("2.2.2.2", "http://something"));
        servers.add(new ShutdownServer("3.3.3.3", "http://something"));
        servers.add(new ShutdownServer("4.4.4.4", "http://something"));
        servers.add(new ShutdownServer("5.5.5.5", "http://something"));

        fragmentReplacer.replaceMainFragmentWith(new ServersListFragment(servers));
    }
}
