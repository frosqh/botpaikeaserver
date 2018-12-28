package com.frosqh.botpaikeaserver.ts3api;

import com.frosqh.botpaikeaserver.locale.Locale;
import com.frosqh.botpaikeaserver.ts3api.exception.NotACommandException;
import com.frosqh.botpaikeaserver.ts3api.spellchecker.LevenshteinDistance;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class CommandManager {

    private final String[] BASE_COMMANDS;

    private final String[] EASTER_EGGS;

    private final String[] COMPLEX_COMMANDS;

    private Locale locale;

    public CommandManager(Locale locale){
        this.locale = locale;
        BASE_COMMANDS = new String[]{"paikea", "next", "play", "pause", "prev", "toggleautoplay", "info"};
        EASTER_EGGS =  new String[]{this.locale.easterShit(),"ok google", "><", "nan", "no", "nope", "nan", "niet", "nein", "pong", "ping", "plop"};
        COMPLEX_COMMANDS = new String[]{"help", "setVolume"};
    }

    private String preProcess(String cmd) throws NotACommandException {
        if (cmd.charAt(0)!='!')
            throw new NotACommandException();
        else
            return cmd.substring(1);

    }

    public boolean isBase(String cmd) throws NotACommandException {
        cmd = preProcess(cmd);
        return Arrays.asList(BASE_COMMANDS).contains(cmd);
    }

    public boolean isEaster(String cmd){
        return Arrays.asList(EASTER_EGGS).contains(cmd);
    }

    public boolean isComplex(String cmd) throws NotACommandException {
        cmd = preProcess(cmd);
        return Arrays.asList(COMPLEX_COMMANDS).contains(cmd);
    }

    public boolean isHelpable(String cmd) throws NotACommandException {
        return isBase(cmd) ||isComplex(cmd);
    }

    public String isAlmostACommand(String cmd) throws NotACommandException {
        cmd = preProcess(cmd);
        for (String base : BASE_COMMANDS)
            if (LevenshteinDistance.getDistance(base, cmd)<2)
                return base;
        for (String complex : COMPLEX_COMMANDS)
            if (LevenshteinDistance.getDistance(complex, cmd)<2)
                return complex;
        return null;
    }

    public String getUsage(String cmd){
        return "Usage";
    }
}
