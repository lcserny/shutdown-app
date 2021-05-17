package io.github.lcserny.shutdownapp;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.concurrent.*;

import static io.github.lcserny.shutdownapp.UdpShutdownPerformer.DEFAULT_SOCKET_TIMEOUT;
import static io.github.lcserny.shutdownapp.UdpShutdownPerformer.SOCKET_TIMEOUT_KEY;

class UdpSocketExecutor {

    private final UdpClient client;
    private final SharedPreferences preferences;

    UdpSocketExecutor(UdpClient client, SharedPreferences preferences) {
        this.client = client;
        this.preferences = preferences;
    }

    String execute(final String payload) {
        int socketTimeout = preferences.getInt(SOCKET_TIMEOUT_KEY, DEFAULT_SOCKET_TIMEOUT);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return client.execute(payload);
            }
        });
        try {
            return future.get(socketTimeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            Log.e(UdpShutdownPerformer.class.getSimpleName(), "Error occurred executing udp socket command", e);
            return e.getMessage();
        }
    }
}
