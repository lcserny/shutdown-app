package io.github.lcserny.shutdownapp;

import java.util.List;
import java.util.Map;

interface NetworkScanner {

    Map<String, String> scanForIPsWithListenPort(String port, String subnetToInclude, List<String> hostsToFind);
}
