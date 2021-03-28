package io.github.lcserny.shutdownapp;

import androidx.room.TypeConverter;

import java.util.Date;

public class EntityDateConverter {

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
