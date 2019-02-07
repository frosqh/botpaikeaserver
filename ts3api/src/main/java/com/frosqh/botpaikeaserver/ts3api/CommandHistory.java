package com.frosqh.botpaikeaserver.ts3api;

import java.util.ArrayList;
import java.util.List;

public class CommandHistory {

    private class CommandEntry{
        public String command;
        public String user;
        public String[] params;

        public CommandEntry(String command, String user, String... params){
            this.command = command;
            this.user = user;
            this.params = params;
        }
    }

    private List<CommandEntry> history;

    public CommandHistory(){
        history = new ArrayList<>();
    }

    public void append(CommandEntry ce){
        history.add(ce);
    }

    public void append(String command, String user, String... params){
        append(new CommandEntry(command, user, params));
    }
}
