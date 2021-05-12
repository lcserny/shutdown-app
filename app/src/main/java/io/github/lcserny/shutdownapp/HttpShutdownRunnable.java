package io.github.lcserny.shutdownapp;

import android.util.Log;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

class HttpShutdownRunnable implements Runnable {

    private final OkHttpClient httpClient;
    private final Request request;

    private String response = "N/A";

    HttpShutdownRunnable(OkHttpClient httpClient, Request request) {
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
            Log.e(HttpShutdownRunnable.class.getSimpleName(), e.getMessage());
            response = e.getMessage();
        }
    }

    String getResponse() {
        return response;
    }
}
