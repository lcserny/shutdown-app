package io.github.lcserny.shutdownapp;

import android.content.SharedPreferences;

import java.util.List;

import static io.github.lcserny.shutdownapp.EnableLOgPersistenceOnCheckedListener.LOG_PERSISTENCE_KEY;

class LogPersistenceEnabledDaoWrapper {

    private final SharedPreferences preferences;
    private final LogEntryDAO logEntryDAO;

    LogPersistenceEnabledDaoWrapper(SharedPreferences preferences, LogEntryDAO logEntryDAO) {
        this.preferences = preferences;
        this.logEntryDAO = logEntryDAO;
    }

    void insert(LogEntry logEntry) {
        boolean logPersistenceEnabled = preferences.getBoolean(LOG_PERSISTENCE_KEY, false);
        if (logPersistenceEnabled) {
            logEntryDAO.insert(logEntry);
        }
    }

    void insert(List<LogEntry> logEntries) {
        boolean logPersistenceEnabled = preferences.getBoolean(LOG_PERSISTENCE_KEY, false);
        if (logPersistenceEnabled) {
            logEntryDAO.insert(logEntries);
        }
    }
}
