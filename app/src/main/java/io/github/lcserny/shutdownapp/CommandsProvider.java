package io.github.lcserny.shutdownapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;

import java.util.Collections;
import java.util.List;

class CommandsProvider {

    private CommandsProvider() {
    }

    static List<Command> provide(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        Command shutdownCommand = new Command(R.string.shutdown_button_label, new ShutdownFragment(new UdpShutdownPerformer(preferences, wifiManager)));
        return Collections.singletonList(shutdownCommand);
    }
}
