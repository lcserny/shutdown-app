package io.github.lcserny.shutdownapp;

import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

class ServersProvider {

    private final DatabaseReference firebaseDatabase;
    private final WifiManager wifiManager;
    private final SharedPreferences preferences;

    public ServersProvider(WifiManager wifiManager, SharedPreferences preferences, DatabaseReference firebaseDatabase) {
        this.wifiManager = wifiManager;
        this.preferences = preferences;
        this.firebaseDatabase = firebaseDatabase;
    }

    public List<ServerWrapper> getServers() throws Exception {
        List<ServerWrapper> serverWrapperList = new ArrayList<>();
        FirebaseServersFinder serversFinder = new FirebaseServersFinder(firebaseDatabase, wifiManager, preferences);
        for (Server server : serversFinder.findServers()) {
            ServerWrapper wrapper = new ServerWrapper(server.getName(), new CommandsListFragment(server.getCommandList()));
            serverWrapperList.add(wrapper);
        }
        return serverWrapperList;
    }
}
