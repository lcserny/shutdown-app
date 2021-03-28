package io.github.lcserny.shutdownapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.ViewHolder> {

    private final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private final List<LogEntry> latestLogs;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView logTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            logTextView = itemView.findViewById(R.id.logTextView);
        }
    }

    LogsAdapter(List<LogEntry> latestLogs) {
        this.latestLogs = latestLogs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View logItem = inflater.inflate(R.layout.log_item, parent, false);
        return new ViewHolder(logItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LogEntry logEntry = latestLogs.get(position);
        String text = String.format("[%s] %s", dateFormat.format(logEntry.getDate()), logEntry.getText());
        holder.logTextView.setText(text);
    }

    @Override
    public int getItemCount() {
        return latestLogs.size();
    }
}
