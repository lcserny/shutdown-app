package io.github.lcserny.shutdownapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ScanFragment extends BackstackFragment {

    private static final String BACKSTACK_NAME = "scanFragment";

    private MainFragmentReplacer fragmentReplacer;
    private Context context;
    private LogPersistenceEnabledDaoWrapper logEntryDAOWrapper;
    private LogEntryDAO logEntryDAO;

    public ScanFragment(LogEntryDAO logEntryDAO) {
        this.logEntryDAO = logEntryDAO;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            this.fragmentReplacer = (MainFragmentReplacer) context;
            this.context = context;
            SharedPreferences preferences = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            this.logEntryDAOWrapper = new LogPersistenceEnabledDaoWrapper(preferences, logEntryDAO);
        }
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.scan, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Setup any handles to view objects here
        super.onViewCreated(view, savedInstanceState);
        EditText portView = view.findViewById(R.id.portView);
        Button scanButton = view.findViewById(R.id.scanView);

        if (context == null) {
            return;
        }

        long start = System.currentTimeMillis();
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        logEntryDAOWrapper.insert(LogEntryConverter.convertForTimeTook("create WifiManager in ScanFragment", start));

        start = System.currentTimeMillis();
        SharedPreferences preferences = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        logEntryDAOWrapper.insert(LogEntryConverter.convertForTimeTook("get SharedPreferences in ScanFragment", start));

        scanButton.setOnClickListener(new ScanOnClickListener(context, fragmentReplacer,
                new CachedLocalNetworkScanner(wifiManager, preferences, logEntryDAOWrapper), portView.getText().toString(), logEntryDAOWrapper));
    }

    @Override
    public String getBackStackName() {
        return BACKSTACK_NAME;
    }
}
