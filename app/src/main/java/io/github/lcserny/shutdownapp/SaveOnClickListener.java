package io.github.lcserny.shutdownapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import static io.github.lcserny.shutdownapp.UdpSocketExecutor.PROXY_PORT_KEY;
import static io.github.lcserny.shutdownapp.UdpSocketExecutor.SOCKET_TIMEOUT_KEY;

class SaveOnClickListener implements View.OnClickListener {

    private final SharedPreferences preferences;
    private final Context context;
    private final Spinner socketTimeoutView;
    private final EditText proxyPortView;

    public SaveOnClickListener(SharedPreferences sharedPreferences, Context context, Spinner socketTimeoutView, EditText proxyPortView) {
        this.preferences = sharedPreferences;
        this.context = context;
        this.socketTimeoutView = socketTimeoutView;
        this.proxyPortView = proxyPortView;
    }

    @Override
    public void onClick(View v) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(SOCKET_TIMEOUT_KEY, Integer.parseInt(socketTimeoutView.getSelectedItem().toString()));
        editor.putInt(PROXY_PORT_KEY, Integer.parseInt(proxyPortView.getText().toString()));
        editor.apply();

        Toast.makeText(context, "Preferences saved!", Toast.LENGTH_SHORT).show();
    }
}
