package io.github.lcserny.shutdownapp;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.concurrent.Callable;

class ToastResponseClickListener implements View.OnClickListener {

    private final Context context;
    private final Callable<String> callable;

    ToastResponseClickListener(Context context, Callable<String> callable) {
        this.context = context;
        this.callable = callable;
    }

    @Override
    public void onClick(View v) {
        try {
            Toast.makeText(context, callable.call(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(ToastResponseClickListener.class.getSimpleName(), "Error occurred showing toast", e);
        }
    }
}
