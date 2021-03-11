package io.github.lcserny.shutdownapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static io.github.lcserny.shutdownapp.LocalNetworkScanner.SOCKET_TIMEOUT_KEY;

class SaveOnClickListener implements View.OnClickListener {

    private final SharedPreferences preferences;
    private final Context context;
    private final EditText socketTimeoutView;

    public SaveOnClickListener(SharedPreferences sharedPreferences, Context context, EditText socketTimeoutView) {
        this.preferences = sharedPreferences;
        this.context = context;
        this.socketTimeoutView = socketTimeoutView;
    }

    @Override
    public void onClick(View v) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(SOCKET_TIMEOUT_KEY, Integer.parseInt(socketTimeoutView.getText().toString()));
        editor.apply();

        Toast.makeText(context, "Preferences saved!", Toast.LENGTH_SHORT).show();
    }
}
