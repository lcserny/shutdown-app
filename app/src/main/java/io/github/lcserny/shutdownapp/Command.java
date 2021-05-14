package io.github.lcserny.shutdownapp;

class Command {

    private final int labelRes;
    private final AbstractBackstackFragment fragment;

    public Command(int labelRes, AbstractBackstackFragment fragment) {
        this.labelRes = labelRes;
        this.fragment = fragment;
    }

    public int getLabelRes() {
        return labelRes;
    }

    public AbstractBackstackFragment getFragment() {
        return fragment;
    }
}
