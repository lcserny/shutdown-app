package io.github.lcserny.shutdownapp;

import android.content.Context;
import androidx.room.Room;

class AppDatabaseFactory {

    private static final String SHUTDOWN_APP_DB = "shutdownApp-db";
    private static AppDatabase appDatabase;

    private AppDatabaseFactory() {
    }

    static synchronized AppDatabase getAppDatabase(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context, AppDatabase.class, SHUTDOWN_APP_DB).build();
        }
        return appDatabase;
    }
}
