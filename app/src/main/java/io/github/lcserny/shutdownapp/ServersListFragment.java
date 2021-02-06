package io.github.lcserny.shutdownapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ServersListFragment extends BackstackFragment {

    private static final String BACKSTACK_NAME = "serversListFragment";

    private final List<ShutdownServer> serverList;

    private MainActivity mainActivity;

    public ServersListFragment(List<ShutdownServer> serverList) {
        this.serverList = serverList;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            this.mainActivity = (MainActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.servers_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.serversRecyclerView);
        recyclerView.setAdapter(new ShutdownServerAdapter(serverList));
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
    }

    @Override
    public String getBackStackName() {
        return BACKSTACK_NAME;
    }
}