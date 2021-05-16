package io.github.lcserny.shutdownapp;

import java.util.concurrent.Callable;

class UdpSocketCallable implements Callable<String> {

    private final UdpClient client;
    private final String payload;

    UdpSocketCallable(UdpClient client, String payload) {
        this.client = client;
        this.payload = payload;
    }

    @Override
    public String call() throws Exception {
        return client.execute(payload);
    }
}
