package io.github.lcserny.shutdownapp;

import android.util.Log;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class HttpShutdownExecutor implements ShutdownExecutor {

    private final OkHttpClient httpClient;
    private final ExecutorService executorService;

    HttpShutdownExecutor(OkHttpClient httpClient) {
        this.httpClient = httpClient;
        this.executorService = Executors.newCachedThreadPool();
    }

    HttpShutdownExecutor(OkHttpClient httpClient, ExecutorService executorService) {
        this.httpClient = httpClient;
        this.executorService = executorService;
    }

    @Override
    public String shutdown(ShutdownServer server, String seconds) {
        if (seconds.startsWith("-")) {
            seconds = "0";
        }

        final Request request = new Request.Builder().url(server.getShutdownUrl() + "?seconds=" + seconds).build();
        final ShutdownRunnable shutdownRunnable = new ShutdownRunnable(httpClient, request);

        executorService.submit(shutdownRunnable);
        executorService.shutdown();
        try {
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Log.e(HttpShutdownExecutor.class.getSimpleName(), e.getMessage());
        }

        return shutdownRunnable.getResponse();
    }

    private static class ShutdownRunnable implements Runnable {

        private final OkHttpClient httpClient;
        private final Request request;

        private String response = "N/A";

        public ShutdownRunnable(OkHttpClient httpClient, Request request) {
            this.httpClient = httpClient;
            this.request = request;
        }

        @Override
        public void run() {
            try (Response httpResponse = httpClient.newCall(request).execute()) {
                final ResponseBody responseBody = httpResponse.body();
                final int statusCode = httpResponse.code();
                if (200 == statusCode && responseBody != null) {
                    String responseBodyString = responseBody.string();
                    if ("".equals(responseBodyString.trim())) {
                        response = "Shutting down server";
                    }
                }
            } catch (IOException e) {
                response = e.getMessage();
            }
        }

        public String getResponse() {
            return response;
        }
    }
}
