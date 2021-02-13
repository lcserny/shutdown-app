package io.github.lcserny.shutdownapp;

import android.util.Log;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class CachedThreadsExecutor {

    private final ExecutorService executorService;

    CachedThreadsExecutor() {
        executorService = Executors.newCachedThreadPool();
    }

    void execute(List<? extends Runnable> runnableList, ExecutionTimeout executionTimeout) {
        for (Runnable runnable : runnableList) {
            executorService.execute(runnable);
        }
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(executionTimeout.getTimeAmount(), executionTimeout.getTimeUnit())) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            Log.e(CachedThreadsExecutor.class.getSimpleName(), e.getMessage());
        }
    }
}
