package io.github.lcserny.shutdownapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LogsFragment extends AbstractDatabaseBacktrackFragment {

    private static final int LOG_AMOUNT = 1500;
    private static final String BACKSTACK_NAME = "logsFragment";

    private Context context;

    public LogsFragment(AppDatabase appDatabase) {
        super(appDatabase);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.logs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final LogEntryDAO logEntryDAO = database.logEntryDAO();
        List<LogDTO> latestLogs = LogEntryConverter.convertEntries(logEntryDAO.getLastN(LOG_AMOUNT));

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
