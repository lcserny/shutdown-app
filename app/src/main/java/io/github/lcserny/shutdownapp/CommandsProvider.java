package io.github.lcserny.shutdownapp;

import android.content.SharedPreferences;
import android.net.wifi.WifiManager;

import com.google.firebase.database.DatabaseReference;
import io.github.lcserny.shutdownapp.shutdown.FirebaseShutdownPerformer;
import io.github.lcserny.shutdownapp.shutdown.ShutdownFragment;
import io.github.lcserny.shutdownapp.shutdown.SimpleShutdownPerformer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Deprecated: commands are provided by server
 */
@Deprecated
class CommandsProvider {

    private static final List<Command> cachedCommands = new CopyOnWriteArrayList<>();

    private CommandsProvider() {
    }

    static List<Command> provide(WifiManager wifiManager, SharedPreferences preferences, DatabaseReference firebaseDatabase) {
        if (cachedCommands.isEmpty()) {
            initCommands(wifiManager, preferences, firebaseDatabase);
        }
        return cachedCommands;
    }

    private static void initCommands(WifiManager wifiManager, SharedPreferences preferences, DatabaseReference firebaseDatabase) {
        SimpleShutdownPerformer performer = new SimpleShutdownPerformer(wifiManager, preferences);
        Command shutdownCommand = new Command("Shutdown", new ShutdownFragment(performer));

        cachedCommands.add(shutdownCommand);
    }
}
