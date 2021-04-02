package io.github.lcserny.shutdownapp;

abstract class AbstractDatabaseBacktrackFragment extends AbstractBackstackFragment {

    protected final AppDatabase database;

    protected AbstractDatabaseBacktrackFragment(AppDatabase database) {
        this.database = database;
    }
}
