package io.github.lcserny.shutdownapp;

import androidx.room.Dao;
import androidx.room.Delete;
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

    @Delete
    void delete(List<LogEntry> logEntries);

    @Query("DELETE FROM LogEntry")
    void deleteAll();

    @Query("DELETE FROM LogEntry WHERE uid NOT IN (SELECT uid FROM LogEntry ORDER BY uid DESC LIMIT :n)")
    void deleteAllButLastNEntries(int n);
}
