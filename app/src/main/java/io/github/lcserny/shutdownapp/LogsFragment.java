package io.github.lcserny.shutdownapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LogsFragment extends BackstackFragment {

    private static final String BACKSTACK_NAME = "logsFragment";

    private Context context;
    private SharedPreferences preferences;

    private final List<LogEntry> latestLogs;

    public LogsFragment() {
        // TODO: send in constructor storage object to get latest 250 logs from
        this.latestLogs = new ArrayList<>();
        this.latestLogs.add(new LogEntry("A log entry"));
        this.latestLogs.add(new LogEntry("Another entry"));
        this.latestLogs.add(new LogEntry("a Really long entry here, a Really long entry here, a Really long entry here, a Really long entry here, a Really long entry here"));
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
        return inflater.inflate(R.layout.logs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView logsRecyclerVew = view.findViewById(R.id.logsRecyclerView);
        logsRecyclerVew.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        logsRecyclerVew.setAdapter(new LogsAdapter(latestLogs));
        logsRecyclerVew.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public String getBackStackName() {
        return BACKSTACK_NAME;
    }
}
