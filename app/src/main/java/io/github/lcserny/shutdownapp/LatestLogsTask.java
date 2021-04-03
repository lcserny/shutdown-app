package io.github.lcserny.shutdownapp;

import android.os.AsyncTask;

import java.util.List;

class LatestLogsTask extends AsyncTask<Void, Void, List<LogDTO>> {

    private static final int LOG_AMOUNT = 1500;

    private final LogEntryDAO logEntryDAO;
    private final ResultCallback<List<LogDTO>> resultCallback;

    LatestLogsTask(LogEntryDAO logEntryDAO, ResultCallback<List<LogDTO>> resultCallback) {
        this.logEntryDAO = logEntryDAO;
        this.resultCallback = resultCallback;
    }

    @Override
    protected List<LogDTO> doInBackground(Void... voids) {
        return LogEntryConverter.convertEntries(logEntryDAO.getLastN(LOG_AMOUNT));
    }

    @Override
    protected void onPostExecute(List<LogDTO> logDTOList) {
        resultCallback.run(logDTOList);
    }
}
