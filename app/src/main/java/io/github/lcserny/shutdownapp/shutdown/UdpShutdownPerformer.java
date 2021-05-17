package io.github.lcserny.shutdownapp.shutdown;

import android.text.TextUtils;
import io.github.lcserny.shutdownapp.UdpSocketExecutor;

public class UdpShutdownPerformer implements ShutdownPerformer {

    private final UdpSocketExecutor executor;

    public UdpShutdownPerformer(UdpSocketExecutor executor) {
        this.executor = executor;
    }

    @Override
    public String shutdown(String seconds) {
        if (!TextUtils.isDigitsOnly(seconds) || seconds.contains("-")) {
            seconds = "0";
        }
        String result = executor.execute("shutdown/seconds=" + seconds);
        if ("OK".equals(result.trim())) {
            return "Shutting down server";
        }
        return result;
    }
}
