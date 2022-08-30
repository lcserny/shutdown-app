package io.github.lcserny.shutdownapp;

import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.util.Log;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import io.github.lcserny.shutdownapp.shutdown.FirebaseShutdownPerformer;
import io.github.lcserny.shutdownapp.shutdown.ShutdownFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FirebaseServersFinder {

    private static final long timeOffset = 60000; // 1 minute in millis

    private final DatabaseReference firebaseDatabase;
    private final WifiManager wifiManager;
    private final SharedPreferences preferences;

    public FirebaseServersFinder(DatabaseReference firebaseDatabase,
                                 WifiManager wifiManager,
                                 SharedPreferences preferences) {
        this.firebaseDatabase = firebaseDatabase;
        this.wifiManager = wifiManager;
        this.preferences = preferences;
    }

    public List<Server> findServers() throws Exception {
        List<Server> servers = new ArrayList<>();

        DataSnapshot snapshot = Tasks.await(firebaseDatabase
                .child("servers")
                .get(), 5, TimeUnit.SECONDS);
        GenericTypeIndicator<Map<String, FirebaseServer>> serversType =
                new GenericTypeIndicator<Map<String, FirebaseServer>>() {};
        Map<String, FirebaseServer> serverMap = snapshot.getValue(serversType);

        for (Map.Entry<String, FirebaseServer> serverEntry : serverMap.entrySet()) {
            FirebaseServer firebaseServer = serverEntry.getValue();

            long currentTime = System.currentTimeMillis();
            if (currentTime > firebaseServer.getLastPingDate() + timeOffset) {
                Log.d("SERVER", "Server '" + serverEntry.getKey() + "' is offline");
                continue;
            }

            Server server = new Server();
            server.setName(serverEntry.getKey());
            List<Command> commands = new ArrayList<>();
            for (String command : firebaseServer.getActionsAvailable()) {
                commands.add(mapCommand(command, serverEntry.getKey()));
            }
            server.setCommandList(commands);

            servers.add(server);
        }

        return servers;
    }

    private Command mapCommand(String type, String server) {
        switch (type) {
            case "shutdown":
                return new Command("shutdown",
                        new ShutdownFragment(
                                new FirebaseShutdownPerformer(firebaseDatabase, server)));
            default:
                throw new RuntimeException("Command '" + type + "' not implemented.");
        }
    }
}
