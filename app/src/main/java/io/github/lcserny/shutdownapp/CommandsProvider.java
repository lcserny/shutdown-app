package io.github.lcserny.shutdownapp;

import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import io.github.lcserny.shutdownapp.shutdown.HttpShutdownClient;
import io.github.lcserny.shutdownapp.shutdown.HttpShutdownExecutor;
import io.github.lcserny.shutdownapp.shutdown.ShutdownFragment;
import io.github.lcserny.shutdownapp.shutdown.SimpleShutdownPerformer;
import okhttp3.OkHttpClient;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import static io.github.lcserny.shutdownapp.UdpServerIpFinder.DEFAULT_SOCKET_TIMEOUT;
import static io.github.lcserny.shutdownapp.UdpServerIpFinder.SOCKET_TIMEOUT_KEY;

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
        UdpFindIPExecutor findIpExecutor = new UdpFindIPExecutor(new UdpServerIpFinder(wifiManager, preferences));
        HttpShutdownExecutor shutdownExecutor = new HttpShutdownExecutor(new HttpShutdownClient(preferences));
        SimpleShutdownPerformer performer = new SimpleShutdownPerformer(findIpExecutor, shutdownExecutor);
        Command shutdownCommand = new Command(R.string.shutdown_button_label, new ShutdownFragment(performer));

        cachedCommands.add(shutdownCommand);
    }
}
