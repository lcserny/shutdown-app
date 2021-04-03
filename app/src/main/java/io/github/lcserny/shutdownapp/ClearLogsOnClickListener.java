package io.github.lcserny.shutdownapp;

import android.os.AsyncTask;
import android.view.View;

class ClearLogsOnClickListener implements View.OnClickListener {

    private final LogEntryDAO logEntryDAO;

    ClearLogsOnClickListener(LogEntryDAO logEntryDAO) {
        this.logEntryDAO = logEntryDAO;
    }

    @Override
    public void onClick(View v) {
        new ClearLogsTask(logEntryDAO).execute();
    }

    private static class ClearLogsTask extends AsyncTask<Void, Void, Void> {

        private final LogEntryDAO logEntryDAO;

        private ClearLogsTask(LogEntryDAO logEntryDAO) {
            this.logEntryDAO = logEntryDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            logEntryDAO.deleteAll();
            return null;
        }
    }
}
