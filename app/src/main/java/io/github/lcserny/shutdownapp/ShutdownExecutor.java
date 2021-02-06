package io.github.lcserny.shutdownapp;

interface ShutdownExecutor {
    String shutdown(ShutdownServer server, String seconds);
}
