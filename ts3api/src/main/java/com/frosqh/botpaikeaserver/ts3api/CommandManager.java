package com.frosqh.botpaikeaserver.ts3api;

import com.frosqh.botpaikeaserver.locale.Locale;
import com.frosqh.botpaikeaserver.ts3api.exception.NotACommandException;
import com.frosqh.botpaikeaserver.ts3api.spellchecker.LevenshteinDistance;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class CommandManager {

    private final String[] baseCommands;

    private final String[] easterEggs;

    private final String[] complexCommands;

    private Locale locale;

    private final CommandHistory commandHistory;

    public CommandManager(Locale locale){
        this.commandHistory = new CommandHistory();
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

    public String execEaster(String cmd){
        switch(cmd){
            case "ok google":
                return locale.easterGoogleResponse();
            case "><":
                return ":D";
            case "nan":
            case "nope":
            case "no":
            case "niet":
            case "non":
            case "nein":
                return locale.easterNoResponse();
            case "pong":
                return "Ping ?";
            case "ping":
                return "Pong !";
            case "plop":
                return locale.easterPlopResponse();
            default:
                if (locale.easterShit().equals(cmd))
                    return locale.easterShitResponse();
                return locale.easterUndefinedBehavior();
        }
    }

    public String execBase(String command) {
        String rep = "default";
        try {
            String cmd = preProcess(command);
            switch (cmd){
                case "help":
                    rep = locale.list();
                    for (String com : baseCommands){
                        String desc = (String) locale.getClass().getDeclaredMethod("desc"+com.toLowerCase(),null).invoke(locale,null);
                        rep+="\t\t• !"+com+" : "+desc+"\n";
                    }
                    for (String com : complexCommands){
                        String desc = (String) locale.getClass().getDeclaredMethod("desc"+com.toLowerCase(),null).invoke(locale,null);
                        rep+="\t\t• !"+com+" : "+desc+"\n";
                    }
                    rep+=locale.seeMore();
                    break;
                case "paikea":
                    rep = locale.paikeSong();
                    break;
                case "next":
                    rep = locale.undefinedBehavior();
                    break;
                case "prev":
                    rep = locale.undefinedBehavior();
                    break;
                case "pause":
                    rep = locale.undefinedBehavior();
                    break;
                case "play":
                    rep = locale.undefinedBehavior();
                    break;
                case "info":
                    rep = locale.undefinedBehavior();
                    break;
                default:
                    rep = locale.undefinedBehavior();
                    break;
            }
        } catch (NotACommandException ignored) {
            return "‼"; //Should not happend and be checked before !
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        //TODO Lier au serveur audio dès que celui-ci sera réalisé.
        return rep;
    }

    public String execComplex(String command) {
        try {
            String cmd = preProcess(command);
        } catch (NotACommandException ignored) {
            return "‼"; //Should not happend and be checked before !
        }
        //TODO Lier au serveur audio dès que possible (aussi faire !help, mais la flemme quoi :D)
        return "Complex !";
    }
}
