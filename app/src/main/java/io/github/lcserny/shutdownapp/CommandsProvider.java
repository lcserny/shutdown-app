package io.github.lcserny.shutdownapp;

import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import io.github.lcserny.shutdownapp.shutdown.ShutdownFragment;
import io.github.lcserny.shutdownapp.shutdown.UdpShutdownPerformer;

import java.util.Collections;
import java.util.List;

class CommandsProvider {

    private CommandsProvider() {
    }

    static List<Command> provide(WifiManager wifiManager, SharedPreferences preferences) {
        return Collections.singletonList(provideShutdownFragment(wifiManager, preferences));
    }

    private static Command provideShutdownFragment(WifiManager wifiManager, SharedPreferences preferences) {
        UdpClient client = new UdpClient(wifiManager, preferences);
        UdpSocketExecutor executor = new UdpSocketExecutor(client, preferences);
        UdpShutdownPerformer performer = new UdpShutdownPerformer(executor);
        return new Command(R.string.shutdown_button_label, new ShutdownFragment(performer));
    }
}
