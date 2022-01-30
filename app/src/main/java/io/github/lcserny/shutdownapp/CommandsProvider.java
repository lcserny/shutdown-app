package io.github.lcserny.shutdownapp;

import android.content.SharedPreferences;
import android.net.nsd.NsdManager;
import io.github.lcserny.shutdownapp.shutdown.ShutdownFragment;
import io.github.lcserny.shutdownapp.shutdown.SimpleShutdownPerformer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class CommandsProvider {

    private static final List<Command> cachedCommands = new CopyOnWriteArrayList<>();

    private CommandsProvider() {
    }

    static List<Command> provide(NsdManager nsdManager, SharedPreferences preferences) {
        if (cachedCommands.isEmpty()) {
            initCommands(nsdManager, preferences);
        }
        return cachedCommands;
    }

    private static void initCommands(NsdManager nsdManager, SharedPreferences preferences) {
        SimpleShutdownPerformer performer = new SimpleShutdownPerformer(nsdManager, preferences);
        Command shutdownCommand = new Command(R.string.shutdown_button_label, new ShutdownFragment(performer));

        cachedCommands.add(shutdownCommand);
    }
}
