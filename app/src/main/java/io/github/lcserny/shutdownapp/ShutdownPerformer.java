package io.github.lcserny.shutdownapp;

interface ShutdownPerformer {
    String shutdown(ShutdownServer server, String seconds);
}
