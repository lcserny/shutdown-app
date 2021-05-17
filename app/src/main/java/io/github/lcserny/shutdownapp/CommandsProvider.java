package io.github.lcserny.shutdownapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import io.github.lcserny.shutdownapp.shutdown.ShutdownFragment;
import io.github.lcserny.shutdownapp.shutdown.UdpShutdownPerformer;

import java.util.Collections;
import java.util.List;

class CommandsProvider {

    private CommandsProvider() {
    }

    static List<Command> provide(Context context) {
        return Collections.singletonList(provideShutdownFragment(context));
    }

    private static Command provideShutdownFragment(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        UdpClient client = new UdpClient(wifiManager, preferences);
        UdpSocketExecutor executor = new UdpSocketExecutor(client, preferences);
        UdpShutdownPerformer performer = new UdpShutdownPerformer(executor);
        return new Command(R.string.shutdown_button_label, new ShutdownFragment(performer));
    }
}
