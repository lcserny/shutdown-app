package io.github.lcserny.shutdownapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ErrorFragment extends AbstractBackstackFragment {

    private static final String BACKSTACK_NAME = "errorFragment";

    private final Exception exception;

    public ErrorFragment(Exception exception) {
        this.exception = exception;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.error, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final TextView errorTextView = view.findViewById(R.id.errorTextView);
        String text = exception.getMessage() + ". Please try again.";
        errorTextView.setText(text);
    }

    @Override
    public String getBackStackName() {
        return BACKSTACK_NAME;
    }
}
