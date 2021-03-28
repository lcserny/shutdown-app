package io.github.lcserny.shutdownapp;

import android.view.View;

class ClearLogsOnClickListener implements View.OnClickListener {

    private final LogEntryDAO logEntryDAO;

    ClearLogsOnClickListener(LogEntryDAO logEntryDAO) {
        this.logEntryDAO = logEntryDAO;
    }

    @Override
    public void onClick(View v) {
        logEntryDAO.deleteAll();
    }
}
