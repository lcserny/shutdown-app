package io.github.lcserny.shutdownapp;

public class ResultPair<T> {

    private final String error;
    private final T result;

    private ResultPair(String error, T result) {
        this.error = error;
        this.result = result;
    }

    public boolean isSuccess() {
        return this.error == null;
    }

    public String getError() {
        return error;
    }

    public T getResult() {
        return result;
    }

    public static class ResultPairBuilder {

        private ResultPairBuilder() {
        }

        public static <T> ResultPair<T> success(T result) {
            return new ResultPair<>(null, result);
        }

        public static <T> ResultPair<T> failure(String error) {
            return new ResultPair<>(error, null);
        }
    }
}
