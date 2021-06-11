package io.github.lcserny.shutdownapp;

import java.net.InetAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UdpFindIPExecutor {

    private final UdpServerIpFinder ipFinder;

    public UdpFindIPExecutor(UdpServerIpFinder ipFinder) {
        this.ipFinder = ipFinder;
    }

    public InetAddress execute() throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<ResultPair<InetAddress>> future = executor.submit(new Callable<ResultPair<InetAddress>>() {
            @Override
            public ResultPair<InetAddress> call() {
                return ipFinder.findIp();
            }
        });
        ResultPair<InetAddress> resultPair = future.get();
        if (!resultPair.isSuccess()) {
            throw new RuntimeException(resultPair.getError());
        }
        return resultPair.getResult();
    }
}
