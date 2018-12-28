package com.frosqh.botpaikeaserver.ts3api;

import com.frosqh.botpaikeaserver.locale.Locale;
import com.frosqh.botpaikeaserver.ts3api.exception.NotACommandException;
import com.frosqh.botpaikeaserver.ts3api.spellchecker.LevenshteinDistance;

import java.util.Arrays;

public class CommandManager {

    private final String[] baseCommands;

    private final String[] easterEggs;

    private final String[] complexCommands;

    private Locale locale;

    public CommandManager(Locale locale){
        this.locale = locale;
        baseCommands = new String[]{"paikea", "next", "play", "pause", "prev", "toggleautoplay", "info"};
        easterEggs =  new String[]{this.locale.easterShit(),"ok google", "><", "nan", "no", "nope", "nan", "niet", "nein", "pong", "ping", "plop"};
        complexCommands = new String[]{"help", "setVolume"};
    }

    private String preProcess(String cmd) throws NotACommandException {
        if (cmd.charAt(0)!='!')
            throw new NotACommandException();
        else
            return cmd.substring(1);

    }

    public boolean isBase(String cmd) throws NotACommandException {
        cmd = preProcess(cmd);
        return Arrays.asList(baseCommands).contains(cmd);
    }

    public boolean isEaster(String cmd){
        return Arrays.asList(easterEggs).contains(cmd);
    }

    public boolean isComplex(String cmd) throws NotACommandException {
        cmd = preProcess(cmd);
        return Arrays.asList(complexCommands).contains(cmd);
    }

    public boolean isHelpable(String cmd) throws NotACommandException {
        return isBase(cmd) ||isComplex(cmd);
    }

    public String isAlmostACommand(String cmd) throws NotACommandException {
        cmd = preProcess(cmd);
        for (String base : baseCommands)
            if (LevenshteinDistance.getDistance(base, cmd)<2)
                return base;
        for (String complex : complexCommands)
            if (LevenshteinDistance.getDistance(complex, cmd)<2)
                return complex;
        return null;
    }

    public String getUsage(String cmd){
        return "Usage";
    }
}
