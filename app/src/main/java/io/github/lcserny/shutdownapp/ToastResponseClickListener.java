package io.github.lcserny.shutdownapp;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

class ToastResponseClickListener implements View.OnClickListener {

    private final Context context;
    private final ResponseCallable<String> callable;

    ToastResponseClickListener(Context context, ResponseCallable<String> callable) {
        this.context = context;
        this.callable = callable;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(context, callable.run(), Toast.LENGTH_SHORT).show();
    }
}
