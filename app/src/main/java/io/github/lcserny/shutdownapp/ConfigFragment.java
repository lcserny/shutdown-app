package io.github.lcserny.shutdownapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static io.github.lcserny.shutdownapp.UdpFindIPExecutor.*;

public class ConfigFragment extends AbstractBackstackFragment {

    private static final String BACKSTACK_NAME = "configFragment";

    private Context context;
    private SharedPreferences preferences;

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
        Spinner socketTimeoutSpinner = view.findViewById(R.id.socketTimeoutSpinner);
        ArrayAdapter<CharSequence> socketTimeoutAdapter = ArrayAdapter.createFromResource(context,
                R.array.socket_timeout_values, android.R.layout.simple_spinner_item);
        socketTimeoutAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        socketTimeoutSpinner.setAdapter(socketTimeoutAdapter);
        socketTimeoutSpinner.setSelection(socketTimeoutAdapter.getPosition(Integer.toString(socketTimeout)));

        int proxyPort = preferences.getInt(PROXY_PORT_KEY, DEFAULT_PROXY_PORT);
        EditText proxyPortView = view.findViewById(R.id.proxyPorView);
        proxyPortView.setText(String.valueOf(proxyPort));

        Button saveBtn = view.findViewById(R.id.saveView);
        saveBtn.setOnClickListener(new SaveOnClickListener(preferences, context, socketTimeoutSpinner, proxyPortView));
    }

    @Override
    public String getBackStackName() {
        return BACKSTACK_NAME;
    }
}
