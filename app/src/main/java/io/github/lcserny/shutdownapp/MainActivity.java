package io.github.lcserny.shutdownapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements MainFragmentReplacer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        replaceMainFragmentWith(new ScanFragment());
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
            replaceMainFragmentWith(new ScanFragment());
            return true;
        } else if (id == R.id.mainMenuConfig) {
            replaceMainFragmentWith(new ConfigFragment());
            return true;
        } else if (id == R.id.mainMenuLogs) {
            replaceMainFragmentWith(new LogsFragment());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void replaceMainFragmentWith(BackstackFragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFragment, fragment);
        transaction.addToBackStack(fragment.getBackStackName());
        transaction.commit();
    }
}