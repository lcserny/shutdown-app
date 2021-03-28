package io.github.lcserny.shutdownapp;

import java.util.Date;

class LogEntry {

    private final Date date;
    private final String text;

    public LogEntry(Date date, String text) {
        this.date = date;
        this.text = text;
    }

    public LogEntry(String text) {
        this(new Date(), text);
    }

    public Date getDate() {
        return date;
    }

    public String getText() {
        return text;
    }
}
