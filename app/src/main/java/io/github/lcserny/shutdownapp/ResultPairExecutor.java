package io.github.lcserny.shutdownapp;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ResultPairExecutor<R> {

    public R execute(Callable<ResultPair<R>> callable) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<ResultPair<R>> future = executor.submit(callable);
        ResultPair<R> result = future.get();
        if (!result.isSuccess()) {
            throw new RuntimeException(result.getError());
        }
        return result.getResult();
    }
}
