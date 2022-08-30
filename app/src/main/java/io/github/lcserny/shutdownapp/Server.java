package io.github.lcserny.shutdownapp;

import java.util.List;

public class Server {

    private String name;
    private List<Command> commandList;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCommandList(List<Command> commandList) {
        this.commandList = commandList;
    }

    public List<Command> getCommandList() {
        return commandList;
    }
}
