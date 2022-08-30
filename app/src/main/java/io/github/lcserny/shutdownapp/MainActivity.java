package io.github.lcserny.shutdownapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MainFragmentReplacer {

    private final Map<Integer, AbstractBackstackFragment> activityFragments = new HashMap<>();

    private DatabaseReference mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFirebase();
        initFragmentsMap();

        replaceMainFragmentInternal(buildMainFragment(), false);
    }

    private AbstractBackstackFragment buildMainFragment() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        SharedPreferences preferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        return new LoadingFragment(() -> {
            try {
                ServersListFragment serversListFragment = new ServersListFragment();
                ServersProvider serversProvider = new ServersProvider(wifiManager, preferences, mFirebaseDatabase);
                List<ServerWrapper> servers = serversProvider.getServers();
                serversListFragment.setServerWrapperList(servers);

                replaceMainFragmentWith(serversListFragment);
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage());
                replaceMainFragmentWith(new ErrorFragment(e));
            }
        });
    }

    private void initFirebase() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if (currentUser == null) {
            mFirebaseAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    // TODO: this might trigger AFTER database is used below?
                    if (task.isSuccessful()) {
                        Log.i("FIREBASE", "signIn:success");
                    } else {
                        Log.e("FIREBASE", "signIn:failure", task.getException());
                    }
                }
            });
        }

        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void initFragmentsMap() {
        activityFragments.put(R.id.mainMenuBack, buildMainFragment());
        activityFragments.put(R.id.mainMenuConfig, new ConfigFragment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        for (Map.Entry<Integer, AbstractBackstackFragment> fragmentEntry : activityFragments.entrySet()) {
            if (fragmentEntry.getKey().equals(id)) {
                replaceMainFragmentWith(fragmentEntry.getValue());
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void replaceMainFragmentWith(AbstractBackstackFragment fragment) {
        replaceMainFragmentInternal(fragment, true);
    }

    private void replaceMainFragmentInternal(AbstractBackstackFragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFragment, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(fragment.getBackStackName());
        }
        transaction.commit();
    }
}