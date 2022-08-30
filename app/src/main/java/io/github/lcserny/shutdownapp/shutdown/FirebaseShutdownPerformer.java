package io.github.lcserny.shutdownapp.shutdown;

import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FirebaseShutdownPerformer implements ShutdownPerformer {

    private static final String ACTION = "shutdown";

    private final DatabaseReference firebaseDatabase;
    private final String server;

    public FirebaseShutdownPerformer(DatabaseReference firebaseDatabase, String server) {
        this.firebaseDatabase = firebaseDatabase;
        this.server = server;
    }

    @Override
    public String shutdown(String seconds) {
        if (!TextUtils.isDigitsOnly(seconds) || seconds.contains("-")) {
            seconds = "0";
        }

        try {
            sendShutdownCommandToServer(seconds);
            return "Shutting down command sent";
        } catch (Exception e) {
            Log.e(FirebaseShutdownPerformer.class.getSimpleName(), e.getMessage(), e);
            return e.getMessage();
        }
    }

    // TODO: implement seconds usage
    private void sendShutdownCommandToServer(String seconds) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                DataSnapshot actionsPendingSnapshot = Tasks.await(firebaseDatabase
                        .child("servers")
                        .child(server)
                        .child("actionsPending")
                        .get(), 5, TimeUnit.SECONDS);

                GenericTypeIndicator<List<String>> actionsPendingType =
                        new GenericTypeIndicator<List<String>>() {};
                List<String> actionsPending = actionsPendingSnapshot.getValue(actionsPendingType);
                if (actionsPending == null) {
                    actionsPending = new ArrayList<>();
                }
                actionsPending.add(ACTION);

                Tasks.await(firebaseDatabase
                        .child("servers")
                        .child(server)
                        .child("actionsPending")
                        .setValue(actionsPending), 5, TimeUnit.SECONDS);
            } catch (Exception e) {
                Log.e("FirebaseShutdownPerformer", e.getMessage());
            }
        });
    }
}
