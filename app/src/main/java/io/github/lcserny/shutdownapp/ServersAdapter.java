package io.github.lcserny.shutdownapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ServersAdapter extends RecyclerView.Adapter<ServersAdapter.ViewHolder> {

    private final List<ServerWrapper> serverWrapperList;
    private final MainFragmentReplacer fragmentReplacer;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.simpleItemButtonView);
        }
    }

    ServersAdapter(List<ServerWrapper> serverWrapperList, MainFragmentReplacer fragmentReplacer) {
        this.serverWrapperList = serverWrapperList;
        this.fragmentReplacer = fragmentReplacer;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View serverItem = inflater.inflate(R.layout.simple_item, parent, false);
        return new ViewHolder(serverItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ServerWrapper serverWrapper = serverWrapperList.get(position);
        holder.button.setText(serverWrapper.getLabel());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentReplacer.replaceMainFragmentWith(serverWrapper.getFragment());
            }
        });
    }

    @Override
    public int getItemCount() {
        return serverWrapperList.size();
    }
}
