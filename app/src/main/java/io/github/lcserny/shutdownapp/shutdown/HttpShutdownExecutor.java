package io.github.lcserny.shutdownapp.shutdown;

import io.github.lcserny.shutdownapp.ResultPair;
import io.github.lcserny.shutdownapp.ResultPair.ResultPairBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class HttpShutdownExecutor {

    private final HttpShutdownClient shutdownClient;

    public HttpShutdownExecutor(HttpShutdownClient shutdownClient) {
        this.shutdownClient = shutdownClient;
    }

    public void shutdown(final String ip, final String seconds) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<ResultPair<Void>> future = executor.submit(new Callable<ResultPair<Void>>() {
            @Override
            public ResultPair<Void> call() {
                return shutdownClient.sendShutdown(ip, seconds);
            }
        });
        ResultPair<Void> resultPair = future.get();
        if (!resultPair.isSuccess()) {
            throw new RuntimeException(resultPair.getError());
        }
    }
}
