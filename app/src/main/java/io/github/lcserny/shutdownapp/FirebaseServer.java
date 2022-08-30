package io.github.lcserny.shutdownapp;

import java.util.List;
import java.util.Objects;

public class FirebaseServer {

    private long lastPingDate;
    private List<String> actionsAvailable;
    private List<String> actionsPending;

    public long getLastPingDate() {
        return lastPingDate;
    }

    public void setLastPingDate(long lastPingDate) {
        this.lastPingDate = lastPingDate;
    }

    public List<String> getActionsAvailable() {
        return actionsAvailable;
    }

    public void setActionsAvailable(List<String> actionsAvailable) {
        this.actionsAvailable = actionsAvailable;
    }

    public List<String> getActionsPending() {
        return actionsPending;
    }

    public void setActionsPending(List<String> actionsPending) {
        this.actionsPending = actionsPending;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FirebaseServer that = (FirebaseServer) o;
        return lastPingDate == that.lastPingDate && Objects.equals(actionsAvailable, that.actionsAvailable) && Objects.equals(actionsPending, that.actionsPending);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastPingDate, actionsAvailable, actionsPending);
    }

    @Override
    public String toString() {
        return "FirebaseServer{" +
                "lastPingDate=" + lastPingDate +
                ", actionsAvailable=" + actionsAvailable +
                ", actionsPending=" + actionsPending +
                '}';
    }
}
