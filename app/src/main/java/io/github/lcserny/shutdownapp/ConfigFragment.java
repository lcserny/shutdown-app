package io.github.lcserny.shutdownapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static io.github.lcserny.shutdownapp.EnableLOgPersistenceOnCheckedListener.LOG_PERSISTENCE_KEY;
import static io.github.lcserny.shutdownapp.LocalNetworkScanner.DEFAULT_SOCKET_TIMEOUT;
import static io.github.lcserny.shutdownapp.LocalNetworkScanner.SOCKET_TIMEOUT_KEY;

public class ConfigFragment extends AbstractDatabaseBacktrackFragment {

    private static final String BACKSTACK_NAME = "configFragment";

    private Context context;
    private SharedPreferences preferences;

    public ConfigFragment(AppDatabase appDatabase) {
        super(appDatabase);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        this.preferences = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.config, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int socketTimeout = preferences.getInt(SOCKET_TIMEOUT_KEY, DEFAULT_SOCKET_TIMEOUT);
        EditText socketTimeoutView = view.findViewById(R.id.socketTimeoutView);
        socketTimeoutView.setText(Integer.toString(socketTimeout));

        Button saveBtn = view.findViewById(R.id.saveView);
        saveBtn.setOnClickListener(new SaveOnClickListener(preferences, context, socketTimeoutView));

        boolean logPersistanceEnabled = preferences.getBoolean(LOG_PERSISTENCE_KEY, false);
        CheckBox persistLogsCheckbox = view.findViewById(R.id.enableLogSavingCheck);
        persistLogsCheckbox.setChecked(logPersistanceEnabled);
        persistLogsCheckbox.setOnCheckedChangeListener(new EnableLOgPersistenceOnCheckedListener(preferences));

        Button clearLogsBtn = view.findViewById(R.id.deleteLogsBtn);
        clearLogsBtn.setOnClickListener(new ClearLogsOnClickListener(database.logEntryDAO()));
    }

    @Override
    public String getBackStackName() {
        return BACKSTACK_NAME;
    }
}
