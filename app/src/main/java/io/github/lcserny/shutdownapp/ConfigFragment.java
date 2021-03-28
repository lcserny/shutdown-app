package io.github.lcserny.shutdownapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static io.github.lcserny.shutdownapp.LocalNetworkScanner.DEFAULT_SOCKET_TIMEOUT;
import static io.github.lcserny.shutdownapp.LocalNetworkScanner.SOCKET_TIMEOUT_KEY;

public class ConfigFragment extends BackstackFragment {

    private static final String BACKSTACK_NAME = "configFragment";

    private Context context;
    private SharedPreferences preferences;
    private LogEntryDAO logEntryDAO;

    public ConfigFragment(LogEntryDAO logEntryDAO) {
        this.logEntryDAO = logEntryDAO;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            this.context = context;
            this.preferences = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        }
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

        Button clearLogsBtn = view.findViewById(R.id.deleteLogsBtn);
        clearLogsBtn.setOnClickListener(new ClearLogsOnClickListener(logEntryDAO));
    }

    @Override
    public String getBackStackName() {
        return BACKSTACK_NAME;
    }
}
