package io.github.lcserny.shutdownapp.shutdown;

import android.content.SharedPreferences;
import io.github.lcserny.shutdownapp.ResultPair.EmptyResultPair;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static io.github.lcserny.shutdownapp.UdpServerIpFinder.DEFAULT_SOCKET_TIMEOUT;
import static io.github.lcserny.shutdownapp.UdpServerIpFinder.SOCKET_TIMEOUT_KEY;
import static java.lang.String.format;

public class HttpShutdownClient {

    public static final String PROXY_HTTP_PORT_KEY = "PROXY_HTTP_PORT";
    public static final int DEFAULT_PROXY_HTTP_PORT = 8077;

    private final SharedPreferences preferences;

    public HttpShutdownClient(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    EmptyResultPair sendShutdown(String ip, String seconds) {
        int httpPort = preferences.getInt(PROXY_HTTP_PORT_KEY, DEFAULT_PROXY_HTTP_PORT);
        Request request = new Request.Builder().url(format("http://%s:%d/shutdown?seconds=%s", ip, httpPort, seconds)).build();
        try (Response httpResponse = createClient().newCall(request).execute()) {
            final int statusCode = httpResponse.code();
            if (200 != statusCode) {
                throw new IOException("Http status received: " + statusCode);
            }
        } catch (IOException e) {
            return new EmptyResultPair(e.getMessage());
        }
        return new EmptyResultPair();
    }

    private OkHttpClient createClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(preferences.getInt(SOCKET_TIMEOUT_KEY, DEFAULT_SOCKET_TIMEOUT), TimeUnit.MILLISECONDS)
                .build();
    }
}
