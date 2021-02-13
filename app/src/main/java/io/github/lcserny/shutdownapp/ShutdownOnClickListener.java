package io.github.lcserny.shutdownapp;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

class ShutdownOnClickListener implements View.OnClickListener {

    private final Context context;
    private final ShutdownServer server;
    private final ShutdownPerformer shutdownPerformer;
    private final EditText shutdownSecondsView;

    ShutdownOnClickListener(Context context, ShutdownServer server, ShutdownPerformer shutdownPerformer, EditText shutdownSecondsView) {
        this.context = context;
        this.server = server;
        this.shutdownPerformer = shutdownPerformer;
        this.shutdownSecondsView = shutdownSecondsView;
    }

    @Override
    public void onClick(View v) {
        final String response = shutdownPerformer.shutdown(server, shutdownSecondsView.getText().toString());
        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
    }
}
