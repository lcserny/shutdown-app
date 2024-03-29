package io.github.lcserny.shutdownapp;

class Command {

    private final String label;
    private final AbstractBackstackFragment fragment;

    public Command(String label, AbstractBackstackFragment fragment) {
        this.label = label;
        this.fragment = fragment;
    }

    public String getLabel() {
        return label;
    }

    public AbstractBackstackFragment getFragment() {
        return fragment;
    }
}
