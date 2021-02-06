package io.github.lcserny.shutdownapp;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

class HttpShutdownExecutor implements ShutdownExecutor {

    private final OkHttpClient httpClient;

    HttpShutdownExecutor(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public String shutdown(ShutdownServer server, String seconds) {
        if (seconds.startsWith("-")) {
            seconds = "0";
        }

        String response = "N/A";
        Request request = new Request.Builder().url(server.getShutdownUrl() + "?seconds=" + seconds).build();
        try (Response httpResponse = httpClient.newCall(request).execute()) {
            final ResponseBody responseBody = httpResponse.body();
            if (responseBody != null) {
                response = responseBody.string();
            }
        } catch (IOException e) {
            response = e.getMessage();
        }

        return response;
    }
}
