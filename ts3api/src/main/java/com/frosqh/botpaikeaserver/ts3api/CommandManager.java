package com.frosqh.botpaikeaserver.ts3api;

import com.frosqh.botpaikeaserver.locale.Locale;
import com.frosqh.botpaikeaserver.player.Player;
import com.frosqh.botpaikeaserver.player.exceptions.EmptyHistoryException;
import com.frosqh.botpaikeaserver.player.exceptions.PauseException;
import com.frosqh.botpaikeaserver.player.exceptions.PlayException;
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

    private Player player;

    private Ts3Api parent;

    public CommandManager(Locale locale, Player player, Ts3Api ts3Api){
        this.commandHistory = new CommandHistory();
        this.locale = locale;
        this.player = player;
        this.parent = ts3Api;
        baseCommands = new String[]{"paikea", "next", "play", "pause", "prev", "toggleautoplay", "info"};
        easterEggs =  new String[]{this.locale.easterShit(),"ok google", "><", "nan", "no", "nope", "non", "nan", "niet", "nein", "pong", "ping", "plop"};
        complexCommands = new String[]{"help", "setvolume", "challenge"};
        //TODO Ajouter shǒushìlìng
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
        try {
            return locale.getClass().getDeclaredMethod("usage"+cmd.toLowerCase(), (Class<?>[]) null).invoke(locale, (Object[]) null)
                    +"\n"
                    +  locale.getClass().getDeclaredMethod("desc"+cmd.toLowerCase(), (Class<?>[]) null).invoke(locale, (Object[]) null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
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
                    player.next();
                    rep = locale.nowPlaying(player.getPlaying().getTitle(),player.getPlaying().getArtist());
                    parent.changeName(player.getPlaying());
                    break;
                case "prev":
                    try {
                        player.prev();
                        rep = locale.nowPlaying(player.getPlaying().getTitle(),player.getPlaying().getArtist());
                    } catch (EmptyHistoryException e) {
                        rep = locale.noPrev();
                    }

                    break;
                case "pause":
                    try {
                        player.pause();
                        rep = null;
                    } catch (PauseException e) {
                        rep = locale.errorPause();
                    }
                    break;
                case "play":
                    try {
                        player.play();
                        rep = locale.nowPlaying(player.getPlaying().getTitle(),player.getPlaying().getArtist());
                    } catch (PlayException e) {
                        rep = locale.errorPlay();
                    }
                    break;
                case "info":
                    rep = player.getInfos();
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

    public String execComplex(String command, String caller) {
        try {
            String cmd = preProcess(command);
            String[]args = cmd.split(" ");
            switch (args[0]){
                case "help":
                    String toDetail = args[1];
                    if (isHelpable("!"+toDetail)) return getUsage(toDetail);
                    else return locale.notFound(toDetail);
                case "setvolume":
                    if (args.length != 2)
                        return locale.usagesetvolume();
                    player.setVolume(Double.parseDouble(args[1]));
                    return player.getInfos();
                case "challenge":
                    if (args.length != 2)
                        return locale.usagechallenge();
                    String username = args[1];
                    parent.initChallenge(caller, username);
                    return "";


            }
        } catch (NotACommandException ignored) {
            return "‼"; //Should not happend and be checked before !
        }
        //TODO Lier au serveur audio dès que possible (aussi faire !help, mais la flemme quoi :D)
        return "Complex !";
    }
}
