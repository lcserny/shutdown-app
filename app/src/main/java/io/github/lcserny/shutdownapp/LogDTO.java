package io.github.lcserny.shutdownapp;

import java.util.Date;

class LogDTO {

    private final Date date;
    private final String text;

    public LogDTO(Date date, String text) {
        this.date = date;
        this.text = text;
    }

    public LogDTO(String text) {
        this(new Date(), text);
    }

    public Date getDate() {
        return date;
    }

    public String getText() {
        return text;
    }
}
