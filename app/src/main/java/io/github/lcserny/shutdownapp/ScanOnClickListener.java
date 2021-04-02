package io.github.lcserny.shutdownapp;

import android.content.Context;
import android.view.View;
import android.widget.Toast;
import io.github.lcserny.shutdownapp.NetworkScanningTask.ServerListFinisher;

import java.util.List;

class ScanOnClickListener implements View.OnClickListener {

    private final Context context;
    private final MainFragmentReplacer fragmentReplacer;
    private final NetworkScanner networkScanner;
    private final int portToScan;
    private final LogPersistenceEnabledDaoWrapper logEntryDAO;

    public ScanOnClickListener(Context context, MainFragmentReplacer fragmentReplacer, NetworkScanner networkScanner,
                               String portToScan, LogPersistenceEnabledDaoWrapper logEntryDAO) {
        this.context = context;
        this.fragmentReplacer = fragmentReplacer;
        this.networkScanner = networkScanner;
        this.portToScan = Integer.parseInt(portToScan);
        this.logEntryDAO = logEntryDAO;
    }

    @Override
    public void onClick(View v) {
        new NetworkScanningTask(networkScanner, portToScan, new ServerListFinisher() {
            @Override
            public void finish(List<ShutdownServer> serverList) {
                if (!serverList.isEmpty()) {
                    long start = System.currentTimeMillis();
                    fragmentReplacer.replaceMainFragmentWith(new ServersListFragment(serverList));
                    logEntryDAO.insert(LogEntryConverter.convertForTimeTook("switch to new ServersList fragment", start));
                } else {
                    Toast.makeText(context, "No servers found", Toast.LENGTH_SHORT).show();
                }
            }
        }).execute();
    }
}
