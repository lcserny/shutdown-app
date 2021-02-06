package io.github.lcserny.shutdownapp;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

class ShutdownOnClickListener implements View.OnClickListener {

    private final Context context;
    private final ShutdownServer server;

    ShutdownOnClickListener(Context context, ShutdownServer server) {
        this.context = context;
        this.server = server;
    }

    @Override
    public void onClick(View v) {
        // TODO send shutdown request and show toast
        Toast toast = Toast.makeText(context, server.getShutdownUrl(), Toast.LENGTH_SHORT);
        toast.show();
    }
}
