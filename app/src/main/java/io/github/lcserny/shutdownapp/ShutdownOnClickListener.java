package io.github.lcserny.shutdownapp;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

class ShutdownOnClickListener implements View.OnClickListener {

    private final Context context;
    private final ShutdownServer server;
    private final ShutdownExecutor shutdownExecutor;
    private final EditText shutdownSecondsView;

    ShutdownOnClickListener(Context context, ShutdownServer server, ShutdownExecutor shutdownExecutor, EditText shutdownSecondsView) {
        this.context = context;
        this.server = server;
        this.shutdownExecutor = shutdownExecutor;
        this.shutdownSecondsView = shutdownSecondsView;
    }

    @Override
    public void onClick(View v) {
        final String response = shutdownExecutor.shutdown(server, shutdownSecondsView.getText().toString());
        Toast toast = Toast.makeText(context, response, Toast.LENGTH_SHORT);
        toast.show();
    }
}
