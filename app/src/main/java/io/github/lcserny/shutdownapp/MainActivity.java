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

public class MainActivity extends AppCompatActivity implements MainFragmentReplacer {

    private WifiManager wifiManager;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        preferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.mainFragment, new CommandsListFragment(CommandsProvider.provide(wifiManager, preferences)));
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mainMenuBack) {
            replaceMainFragmentWith(new CommandsListFragment(CommandsProvider.provide(wifiManager, preferences)));
            return true;
        } else if (id == R.id.mainMenuConfig) {
            replaceMainFragmentWith(new ConfigFragment());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void replaceMainFragmentWith(AbstractBackstackFragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFragment, fragment);
        transaction.addToBackStack(fragment.getBackStackName());
        transaction.commit();
    }
}