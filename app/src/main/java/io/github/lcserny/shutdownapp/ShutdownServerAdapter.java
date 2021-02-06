package io.github.lcserny.shutdownapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShutdownServerAdapter extends RecyclerView.Adapter<ShutdownServerAdapter.ViewHolder> {

    private final List<ShutdownServer> serverList;
    private final ShutdownExecutor shutdownExecutor;
    private final EditText shutdownSecondsView;

    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView ipTextView;
        Button shutdownButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ipTextView = itemView.findViewById(R.id.serverIpTextView);
            shutdownButton = itemView.findViewById(R.id.serverIShutdownButtonView);
        }
    }

    ShutdownServerAdapter(List<ShutdownServer> serverList, ShutdownExecutor shutdownExecutor, EditText shutdownSecondsView) {
        this.serverList = serverList;
        this.shutdownExecutor = shutdownExecutor;
        this.shutdownSecondsView = shutdownSecondsView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View serverItem = inflater.inflate(R.layout.server_item, parent, false);
        return new ViewHolder(serverItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ShutdownServer server = serverList.get(position);
        holder.ipTextView.setText(server.getIp());
        holder.shutdownButton.setOnClickListener(new ShutdownOnClickListener(context, server, shutdownExecutor, shutdownSecondsView));
    }

    @Override
    public int getItemCount() {
        return serverList.size();
    }
}
