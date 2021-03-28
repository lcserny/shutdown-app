package io.github.lcserny.shutdownapp;

import android.content.SharedPreferences;
import android.widget.CompoundButton;

class EnableLOgPersistenceOnCheckedListener implements CompoundButton.OnCheckedChangeListener {

    public static final String LOG_PERSISTENCE_KEY = "LOG_PERSISTENCE_ENABLED";

    private final SharedPreferences preferences;

    public EnableLOgPersistenceOnCheckedListener(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(LOG_PERSISTENCE_KEY, isChecked);
        editor.apply();
    }
}
