package io.github.lcserny.shutdownapp;

import java.util.ArrayList;
import java.util.List;

class LogEntryConverter {

    private LogEntryConverter() {
    }

    static LogDTO convert(LogEntry logEntry) {
        return new LogDTO(logEntry.date, logEntry.text);
    }

    static List<LogDTO> convertEntries(List<LogEntry> logEntries) {
        List<LogDTO> logDTOList = new ArrayList<>();
        for (LogEntry logEntry : logEntries) {
            logDTOList.add(convert(logEntry));
        }
        return logDTOList;
    }

    static LogEntry convert(LogDTO logDTO) {
        LogEntry logEntry = new LogEntry();
        logEntry.date = logDTO.getDate();
        logEntry.text = logDTO.getText();
        return logEntry;
    }

    static LogEntry convertForTimeTook(String action, long timeStart) {
        long timeEnd = System.currentTimeMillis();
        String text = String.format("It took %d ms to %s", timeEnd - timeStart, action);
        return convert(new LogDTO(text));
    }

    static List<LogEntry> convertDTOs(List<LogDTO> logDTOList) {
        List<LogEntry> logEntries = new ArrayList<>();
        for (LogDTO logDTO : logDTOList) {
            logEntries.add(convert(logDTO));
        }
        return logEntries;
    }
}
