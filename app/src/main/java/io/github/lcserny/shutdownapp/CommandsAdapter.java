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

public class CommandsAdapter extends RecyclerView.Adapter<CommandsAdapter.ViewHolder> {

    private final List<Command> commandList;
    private final MainFragmentReplacer fragmentReplacer;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.commandButtonView);
        }
    }

    CommandsAdapter(List<Command> commandList, MainFragmentReplacer fragmentReplacer) {
        this.commandList = commandList;
        this.fragmentReplacer = fragmentReplacer;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View serverItem = inflater.inflate(R.layout.command, parent, false);
        return new ViewHolder(serverItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Command command = commandList.get(position);
        holder.button.setText(command.getLabelRes());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentReplacer.replaceMainFragmentWith(command.getFragment());
            }
        });
    }

    @Override
    public int getItemCount() {
        return commandList.size();
    }
}
