package io.github.lcserny.shutdownapp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

class LocalNetworkScanner implements NetworkScanner {

    private static final String SUBNET_TO_INCLUDE = "192.168.100";

    @Override
    public List<String> scanForIPsWithListenPort(final int port) {
        CachedThreadsExecutor executor = new CachedThreadsExecutor();
        List<String> foundHosts = new ArrayList<>();
        List<Runnable> runnableList = new ArrayList<>();
        for (int i = 0; i < 256; i++) {
            String builtAddress = SUBNET_TO_INCLUDE + "." + i;
            runnableList.add(new NetworkScanningRunnable(builtAddress, port, foundHosts));
        }
        executor.execute(runnableList, new ExecutionTimeout(10, TimeUnit.SECONDS));
        return foundHosts;
    }
}
