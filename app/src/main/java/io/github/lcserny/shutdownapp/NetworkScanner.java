package io.github.lcserny.shutdownapp;

import java.util.List;

interface NetworkScanner {
    List<String> scanForIPsWithListenPort(String port);
}
