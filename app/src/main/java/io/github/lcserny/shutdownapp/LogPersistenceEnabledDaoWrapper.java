package io.github.lcserny.shutdownapp;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.util.Arrays;

import static io.github.lcserny.shutdownapp.EnableLOgPersistenceOnCheckedListener.LOG_PERSISTENCE_KEY;

class LogPersistenceEnabledDaoWrapper {

    private final SharedPreferences preferences;
    private final LogEntryDAO logEntryDAO;

    LogPersistenceEnabledDaoWrapper(SharedPreferences preferences, LogEntryDAO logEntryDAO) {
        this.preferences = preferences;
        this.logEntryDAO = logEntryDAO;
    }

    void insert(LogEntry... logEntries) {
        boolean logPersistenceEnabled = preferences.getBoolean(LOG_PERSISTENCE_KEY, false);
        if (logPersistenceEnabled) {
            new InsertLogsTask(logEntryDAO).execute(logEntries);
        }
    }

    private static class InsertLogsTask extends AsyncTask<LogEntry, Void, Void> {

        private final LogEntryDAO logEntryDAO;

        private InsertLogsTask(LogEntryDAO logEntryDAO) {
            this.logEntryDAO = logEntryDAO;
        }

        @Override
        protected Void doInBackground(LogEntry... logEntries) {
            logEntryDAO.insert(Arrays.asList(logEntries));
            return null;
        }
    }
}
