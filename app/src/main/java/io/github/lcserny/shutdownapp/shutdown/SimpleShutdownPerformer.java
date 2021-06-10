package io.github.lcserny.shutdownapp.shutdown;

import android.text.TextUtils;
import android.util.Log;
import io.github.lcserny.shutdownapp.UdpFindIPExecutor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.net.InetAddress;

public class SimpleShutdownPerformer implements ShutdownPerformer {

    private final UdpFindIPExecutor findIPExecutor;
    private final HttpShutdownExecutor shutdownExecutor;

    public SimpleShutdownPerformer(UdpFindIPExecutor findIPExecutor, HttpShutdownExecutor shutdownExecutor) {
        this.findIPExecutor = findIPExecutor;
        this.shutdownExecutor = shutdownExecutor;
    }

    @Override
    public String shutdown(String seconds) {
        try {
            InetAddress address = findIPExecutor.execute();
            String ip = address.getHostAddress();
            if (!TextUtils.isDigitsOnly(seconds) || seconds.contains("-")) {
                seconds = "0";
            }
            shutdownExecutor.shutdown(ip, seconds);
            return "Shutting down server";
        } catch (Exception e) {
            Log.e(SimpleShutdownPerformer.class.getSimpleName(), e.getMessage(), e);
            return e.getMessage();
        }
    }
}
