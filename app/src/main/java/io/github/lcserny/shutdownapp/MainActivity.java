package io.github.lcserny.shutdownapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MainFragmentReplacer {

    private final Map<Integer, AbstractBackstackFragment> activityFragments = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragmentsMap();
        replaceMainFragmentInternal(activityFragments.get(R.id.mainMenuBack), false);
    }

    private void initFragmentsMap() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        SharedPreferences preferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        activityFragments.put(R.id.mainMenuBack, new CommandsListFragment(CommandsProvider.provide(wifiManager, preferences)));
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