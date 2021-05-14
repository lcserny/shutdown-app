package io.github.lcserny.shutdownapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ShutdownFragment extends AbstractBackstackFragment {

    private static final String BACKSTACK_NAME = "shutdownFragment";

    private final ShutdownPerformer shutdownPerformer;
    private Context context;

    public ShutdownFragment(ShutdownPerformer shutdownPerformer) {
        this.shutdownPerformer = shutdownPerformer;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.shutdown, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final EditText shutdownSecondsView = view.findViewById(R.id.shutdownSecondsView);
        Button executeShutdownButton = view.findViewById(R.id.executeShutdownButton);
        executeShutdownButton.setOnClickListener(new ToastResponseClickListener(context, new ResponseCallable<String>() {
            @Override
            public String run() {
                return shutdownPerformer.shutdown(shutdownSecondsView.getText().toString());
            }
        }));
    }

    @Override
    public String getBackStackName() {
        return BACKSTACK_NAME;
    }
}
