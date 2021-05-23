package io.github.lcserny.shutdownapp;

import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import io.github.lcserny.shutdownapp.shutdown.ShutdownFragment;
import io.github.lcserny.shutdownapp.shutdown.UdpShutdownPerformer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class CommandsProvider {

    private static final List<Command> cachedCommands = new CopyOnWriteArrayList<>();

    private CommandsProvider() {
    }

    static List<Command> provide(WifiManager wifiManager, SharedPreferences preferences) {
        if (cachedCommands.isEmpty()) {
            initCommands(wifiManager, preferences);
        }
        return cachedCommands;
    }

    private static void initCommands(WifiManager wifiManager, SharedPreferences preferences) {
        UdpClient client = new UdpClient(wifiManager, preferences);
        UdpSocketExecutor executor = new UdpSocketExecutor(client, preferences);
        UdpShutdownPerformer performer = new UdpShutdownPerformer(executor);
        Command shutdownCommand = new Command(R.string.shutdown_button_label, new ShutdownFragment(performer));

        cachedCommands.add(shutdownCommand);
    }
}
