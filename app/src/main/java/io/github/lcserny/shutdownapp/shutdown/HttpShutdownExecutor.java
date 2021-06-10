package io.github.lcserny.shutdownapp.shutdown;

import android.content.SharedPreferences;
import android.util.Log;
import io.github.lcserny.shutdownapp.ResultPair;
import io.github.lcserny.shutdownapp.ResultPair.ResultPairBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.concurrent.*;

import static io.github.lcserny.shutdownapp.UdpFindIPExecutor.DEFAULT_SOCKET_TIMEOUT;
import static io.github.lcserny.shutdownapp.UdpFindIPExecutor.SOCKET_TIMEOUT_KEY;

public class HttpShutdownExecutor {

    private final OkHttpClient httpClient;
    private final SharedPreferences preferences;

    public HttpShutdownExecutor(OkHttpClient httpClient, SharedPreferences preferences) {
        this.httpClient = httpClient;
        this.preferences = preferences;
    }

    public void shutdown(final String ip, final String seconds) throws Exception {
        int socketTimeout = preferences.getInt(SOCKET_TIMEOUT_KEY, DEFAULT_SOCKET_TIMEOUT);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<ResultPair<Void>> future = executor.submit(new Callable<ResultPair<Void>>() {
            @Override
            public ResultPair<Void> call() {
                Request request = new Request.Builder().url("http://" + ip + ":8077/shutdown?seconds=" + seconds).build();
                try (Response httpResponse = httpClient.newCall(request).execute()) {
                    final int statusCode = httpResponse.code();
                    if (200 != statusCode) {
                        throw new IOException("Http status received: " + statusCode);
                    }
                } catch (IOException e) {
                    return ResultPairBuilder.failure(e.getMessage());
                }
                return ResultPairBuilder.success(null);
            }
        });
        ResultPair<Void> resultPair = future.get(socketTimeout, TimeUnit.MILLISECONDS);
        if (!resultPair.isSuccess()) {
            throw new RuntimeException(resultPair.getError());
        }
    }
}
