package io.github.lcserny.shutdownapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoadingFragment extends AbstractBackstackFragment {

    private static final String BACKSTACK_NAME = "loadingFragment";

    private final Runnable runnable;

    public LoadingFragment(Runnable runnable) {
        this.runnable = runnable;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.loading, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(runnable);
    }

    @Override
    public String getBackStackName() {
        return BACKSTACK_NAME;
    }
}
