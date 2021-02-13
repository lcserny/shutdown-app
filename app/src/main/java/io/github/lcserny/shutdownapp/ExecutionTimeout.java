package io.github.lcserny.shutdownapp;

import java.util.concurrent.TimeUnit;

class ExecutionTimeout {

    private final long timeAmount;
    private final TimeUnit timeUnit;

    ExecutionTimeout(long timeAmount, TimeUnit timeUnit) {
        this.timeAmount = timeAmount;
        this.timeUnit = timeUnit;
    }

    long getTimeAmount() {
        return timeAmount;
    }

    TimeUnit getTimeUnit() {
        return timeUnit;
    }
}
