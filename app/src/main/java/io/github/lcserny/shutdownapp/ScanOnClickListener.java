package io.github.lcserny.shutdownapp;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

class ScanOnClickListener implements View.OnClickListener {

    private static final String SHUTDOWN_PATH_PATTERN = "http://%s:%s/shutdown";

    private final Context context;
    private final MainFragmentReplacer fragmentReplacer;
    private final NetworkScanner networkScanner;
    private final int portToScan;
    private final LogEntryDAO logEntryDAO;

    public ScanOnClickListener(Context context, MainFragmentReplacer fragmentReplacer, NetworkScanner networkScanner,
                               String portToScan, LogEntryDAO logEntryDAO) {
        this.context = context;
        this.fragmentReplacer = fragmentReplacer;
        this.networkScanner = networkScanner;
        this.portToScan = Integer.parseInt(portToScan);
        this.logEntryDAO = logEntryDAO;
    }

    @Override
    public void onClick(View v) {
        List<ShutdownServer> servers = new ArrayList<>();
        // TODO: loading icon in UI?
        for (String host : networkScanner.scanForIPsWithListenPort(portToScan)) {
            servers.add(new ShutdownServer(host, String.format(SHUTDOWN_PATH_PATTERN, host, portToScan)));
        }
        if (!servers.isEmpty()) {
            long start = System.currentTimeMillis();
            fragmentReplacer.replaceMainFragmentWith(new ServersListFragment(servers));
            logEntryDAO.insert(LogEntryConverter.convertForTimeTook("switch to new ServersList fragment", start));
        } else {
            Toast.makeText(context, "No servers found", Toast.LENGTH_SHORT).show();
        }
    }
}
