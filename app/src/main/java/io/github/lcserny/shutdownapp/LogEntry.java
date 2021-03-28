package io.github.lcserny.shutdownapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity
public class LogEntry {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    @TypeConverters(EntityDateConverter.class)
    @ColumnInfo(name = "date")
    public Date date;

    @ColumnInfo(name = "text")
    public String text;
}
