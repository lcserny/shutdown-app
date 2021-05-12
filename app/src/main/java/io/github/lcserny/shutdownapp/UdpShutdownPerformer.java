package io.github.lcserny.shutdownapp;

public class UdpShutdownPerformer implements ShutdownPerformer {

    // TODO: share UDP client/server with network scanner

    @Override
    public String shutdown(ShutdownServer server, String seconds) {
        CachedThreadsExecutor executor = new CachedThreadsExecutor();

        return null;
    }
}
