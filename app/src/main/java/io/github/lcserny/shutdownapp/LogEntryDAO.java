package io.github.lcserny.shutdownapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LogEntryDAO {

    @Query("SELECT * FROM LogEntry ORDER BY date DESC LIMIT :n")
    List<LogEntry> getLastN(int n);

    @Insert
    void insert(LogEntry logEntry);

    @Insert
    void insert(List<LogEntry> logEntries);

    @Query("DELETE FROM LogEntry")
    void deleteAll();
}
