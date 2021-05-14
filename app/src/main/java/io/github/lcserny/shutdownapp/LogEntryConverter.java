package io.github.lcserny.shutdownapp;

class LogEntryConverter {

    private LogEntryConverter() {
    }

    static String convertForTimeTook(String action, long timeStart) {
        long timeEnd = System.currentTimeMillis();
        return String.format("It took %d ms to %s", timeEnd - timeStart, action);
    }
}
