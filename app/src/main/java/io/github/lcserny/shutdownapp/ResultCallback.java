package io.github.lcserny.shutdownapp;

interface ResultCallback<E> {
    void run(E result);
}
