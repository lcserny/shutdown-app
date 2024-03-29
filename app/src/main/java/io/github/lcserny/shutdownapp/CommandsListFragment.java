package io.github.lcserny.shutdownapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommandsListFragment extends AbstractBackstackFragment {

    private static final String BACKSTACK_NAME = "commandsListFragment";
    private static final String TITLE = "Commands";

    private final List<Command> commandsList;

    private MainActivity mainActivity;
    private MainFragmentReplacer fragmentReplacer;

    public CommandsListFragment(List<Command> commandsList) {
        this.commandsList = commandsList;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            this.mainActivity = (MainActivity) context;
        }
        if (context instanceof MainFragmentReplacer) {
            this.fragmentReplacer = (MainFragmentReplacer) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.simple_item_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView titleView = view.findViewById(R.id.listTitleTextView);
        titleView.setText(TITLE);

        RecyclerView recyclerView = view.findViewById(R.id.simpleItemListRecyclerView);
        recyclerView.setAdapter(new CommandsAdapter(commandsList, fragmentReplacer));
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
    }

    @Override
    public String getBackStackName() {
        return BACKSTACK_NAME;
    }
}
