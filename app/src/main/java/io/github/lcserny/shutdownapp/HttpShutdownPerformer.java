package io.github.lcserny.shutdownapp;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

class HttpShutdownPerformer implements ShutdownPerformer {

    private final OkHttpClient httpClient;

    HttpShutdownPerformer(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public String shutdown(ShutdownServer server, String seconds) {
        CachedThreadsExecutor executor = new CachedThreadsExecutor();
        if (seconds.startsWith("-")) {
            seconds = "0";
        }
        Request request = new Request.Builder().url(server.getShutdownUrl() + "?seconds=" + seconds).build();
        HttpShutdownRunnable httpShutdownRunnable = new HttpShutdownRunnable(httpClient, request);
        executor.execute(Collections.singletonList(httpShutdownRunnable), new ExecutionTimeout(10, TimeUnit.SECONDS));
        return httpShutdownRunnable.getResponse();
    }
}
