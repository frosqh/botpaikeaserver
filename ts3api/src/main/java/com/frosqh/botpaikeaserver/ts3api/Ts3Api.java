package com.frosqh.botpaikeaserver.ts3api;

import com.frosqh.botpaikeaserver.database.DataBase;
import com.frosqh.botpaikeaserver.locale.FRFR;
import com.frosqh.botpaikeaserver.locale.Locale;
import com.frosqh.botpaikeaserver.models.Song;
import com.frosqh.botpaikeaserver.player.Player;
import com.frosqh.botpaikeaserver.settings.Settings;
import com.frosqh.botpaikeaserver.ts3api.exception.NotACommandException;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ts3Api {

    private enum STATE_CHALLENGE {
        WAITING_RESPONSE,
        WAITING_PLAY,
        ASKED,
        PLAYED_R,
        PLAYED_P,
        PLAYED_C,
    }

    private TS3Api api;
    private List<String> knownUsers;
    private Locale locale;
    private CommandManager commandManager;
    private Player player;
    private int groupID = -1;
    private List<Pair<Pair<Integer, STATE_CHALLENGE>,Pair<Integer, STATE_CHALLENGE>>> challengeWaitList;



    public Ts3Api(String host, String login, String password, String botName, List<String> users, Locale locale, Player player){
        knownUsers = users;
        this.locale = locale;
        this.player = player;
        player.setMethod(() -> toCalle());
        this.challengeWaitList = new ArrayList<>();
        commandManager = new CommandManager(this.locale, this.player, this);

        // Connecting Query
        TS3Config ts3Config = new TS3Config();
        ts3Config.setHost(host);
        TS3Query ts3Query = new TS3Query(ts3Config);
        ts3Query.connect();
        // Creating API and initializing query
        api = ts3Query.getApi();
        api.login(login,password);
        api.selectVirtualServerById(1);
        api.setNickname(botName);

        goToRightChannel();

        final int clientId = api.whoAmI().getId();

        api.registerEvent(TS3EventType.TEXT_CHANNEL);
        api.registerEvent(TS3EventType.TEXT_PRIVATE);
        api.addTS3Listeners(new OnReceiveListener(clientId));

        List<ServerGroup> l = api.getServerGroups();
        for (ServerGroup sg : l){
            if (sg.getName().contains("♪♪")){
                groupID = sg.getId();
            }
        }
        if (groupID == -1){
            groupID = api.addServerGroup("♪♪");
        }

        welcomeAll();
    }

    private void goToRightChannel(){
        for (String user : knownUsers){
            Client tsClient = api.getClientByNameExact(user, true);
            if (tsClient != null && api.whoAmI().getChannelId() != tsClient.getChannelId()){
                api.moveQuery(tsClient.getChannelId());
                return;
            }
        }
    }

    private void welcomeAll(){
        for (String user : knownUsers){
            Client tsClient = api.getClientByNameExact(user, true);
            if (tsClient != null && !tsClient.isOutputMuted() && !tsClient.isAway()){
                System.out.println("ID : "+ tsClient.getId());
                api.sendPrivateMessage(tsClient.getId(),locale.welcomeMessage());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        com.sun.javafx.application.PlatformImpl.startup(Thread.currentThread());
        Settings.getInstance().load();
        DataBase dataBase = new DataBase();
        dataBase.refreshSongs();
        new Ts3Api(Settings.getInstance().get("sv_address"),Settings.getInstance().get("sv_login"),Settings.getInstance().get("sv_password"),Settings.getInstance().get("bot_name"), Arrays.asList(Settings.getInstance().get("known_users").split(";")),Settings.getInstance().getLocale(), new Player(Settings.getInstance().getLocale()));
    }

    private class OnReceiveListener extends TS3EventAdapter {
        public final int selfID;

        public OnReceiveListener(int self) {
            selfID = self;
        }

        @Override
        public void onTextMessage(TextMessageEvent e){
            for (Pair<Pair<Integer, STATE_CHALLENGE>,Pair<Integer, STATE_CHALLENGE>> p : challengeWaitList){
                if (p.getValue().getKey().equals(e.getInvokerId()) && p.getValue().getValue() == STATE_CHALLENGE.WAITING_RESPONSE){
                    String response = e.getMessage().toLowerCase();
                    if ("y".equals(response)) {
                        continueChallenge(p.getKey().getKey(), p.getValue().getKey(), challengeWaitList.indexOf(p));
                        return;
                    } else {
                        abortChallenge(p.getKey().getKey(), p.getValue().getKey(), challengeWaitList.indexOf(p));
                        return;
                    }
                } else if (p.getValue().getKey().equals(e.getInvokerId()) && p.getValue().getValue() == STATE_CHALLENGE.WAITING_PLAY) {
                    String response = e.getMessage().toLowerCase();
                    Pair<Integer, STATE_CHALLENGE> p1 = p.getKey();
                    Pair<Integer, STATE_CHALLENGE> p2 = null;
                    boolean isOk = true;
                    switch (response){
                        case "1":
                             p2 = new Pair<>(p.getValue().getKey(), STATE_CHALLENGE.PLAYED_P);
                            break;
                        case "2":
                            p2 = new Pair<>(p.getValue().getKey(), STATE_CHALLENGE.PLAYED_R);
                            break;
                        case "3":
                            p2 = new Pair<>(p.getValue().getKey(), STATE_CHALLENGE.PLAYED_C);
                            break;
                        default:
                            isOk = false;
                            break;
                    }
                    if (isOk){
                        if (p1.getValue() == STATE_CHALLENGE.WAITING_PLAY){
                            challengeWaitList.remove(p);
                            challengeWaitList.add(new Pair<>(p1,p2));
                        } else {
                            challengeWaitList.remove(p);
                            finalizeChallenge(new Pair<>(p1,p2));
                        }
                        return;
                    }
                } else if (p.getKey().getKey().equals(e.getInvokerId()) && p.getKey().getValue() == STATE_CHALLENGE.WAITING_PLAY) {
                    String response = e.getMessage().toLowerCase();
                    Pair<Integer, STATE_CHALLENGE> p2 = p.getValue();
                    Pair<Integer, STATE_CHALLENGE> p1 = null;
                    boolean isOk = true;
                    switch (response){
                        case "1":
                            p1 = new Pair<>(p.getKey().getKey(), STATE_CHALLENGE.PLAYED_P);
                            break;
                        case "2":
                            p1 = new Pair<>(p.getKey().getKey(), STATE_CHALLENGE.PLAYED_R);
                            break;
                        case "3":
                            p1 = new Pair<>(p.getKey().getKey(), STATE_CHALLENGE.PLAYED_C);
                            break;
                        default:
                            isOk = false;
                            break;
                    }
                    if (isOk){
                        if (p2.getValue() == STATE_CHALLENGE.WAITING_PLAY){
                            challengeWaitList.remove(p);
                            challengeWaitList.add(new Pair<>(p1,p2));
                        } else {
                            challengeWaitList.remove(p);
                            finalizeChallenge(new Pair<>(p1,p2));
                        }
                        return;
                    }
                }
            }
            if (e.getTargetMode()== TextMessageTargetMode.CHANNEL && e.getInvokerId()!=selfID){
                int id = e.getInvokerId();
                String command = e.getMessage().toLowerCase();
                if ("!invoke".equals(command))
                    api.sendPrivateMessage(id,locale.welcomeMessage());
            }
            if (e.getTargetMode() == TextMessageTargetMode.CLIENT && e.getInvokerId()!=selfID){
                int id = e.getInvokerId();
                String command = e.getMessage().toLowerCase();
                String[] args = command.split(" ");
                String ans = null;

                if (commandManager.isEaster(command))
                    ans = commandManager.execEaster(command);
                else {
                    try {
                        if (commandManager.isBase(command) ||args[0].equals("!help")&&args.length<2)
                            ans=commandManager.execBase(command);
                        else if (commandManager.isComplex(args[0]))
                            ans = commandManager.execComplex(command, e.getInvokerName());
                        else
                            ans = "‼";
                    } catch (NotACommandException ignored) {
                        ans = "‼";
                    }
                }

                if (!"‼".equals(ans)){
                    api.sendPrivateMessage(id, ans);
                } else {
                    api.sendPrivateMessage(id, locale.notFound(args[0]));
                }
            }
        }
    }

    public void initChallenge(String challenger, String challengee){
        int challengerID = api.getClientByNameExact(challenger, true).getId();
        int challengeeID = api.getClientByNameExact(challengee, true).getId();
        System.out.println(challengee + " : " + challengeeID);
        System.out.println(challenger + " : " + challengerID);
        api.sendPrivateMessage(challengeeID, locale.challengeAsking());
        challengeWaitList.add(new Pair<>(new Pair<>(challengerID,STATE_CHALLENGE.ASKED),new Pair<>(challengeeID,STATE_CHALLENGE.WAITING_RESPONSE)));
    }

    public void continueChallenge(int challengerID, int challengeeID, int index){
        api.sendPrivateMessage(challengeeID, locale.challengeChoice());
        api.sendPrivateMessage(challengerID, locale.challengeChoice());
        challengeWaitList.remove(index);
        challengeWaitList.add(new Pair<>(new Pair<>(challengerID,STATE_CHALLENGE.WAITING_PLAY),new Pair<>(challengeeID,STATE_CHALLENGE.WAITING_PLAY)));
    }

    public void abortChallenge(int challengerID, int challengeeID, int index){
        challengeWaitList.remove(index);
        api.sendPrivateMessage(challengerID, locale.challengeAbort());
    }

    private void finalizeChallenge(Pair<Pair<Integer, STATE_CHALLENGE>,Pair<Integer, STATE_CHALLENGE>> p) {
        /*final String rock =
                "    ______        \n" +
                "---'   ____)      \n" +
                "      (_____)     \n" +
                "      (_____)     \n" +
                "      (____)      \n" +
                "---.__(___)       ";
        final String paper =
                "     _______      \n" +
                "---'    ____)____ \n" +
                "           ______)\n" +
                "          _______)\n" +
                "         _______) \n" +
                "---.__________)   ";
        final String scissors =
                "    _______       \n" +
                "---'   ____)____  \n" +
                "          ______) \n" +
                "       __________)\n" +
                "      (____)      \n" +
                "---.__(___)       ";*/
        final String scissors = "\uD83D\uDD96";
        final String rock = "✊";
        final String paper = "✋";
        Integer challengerID = p.getKey().getKey();
        Integer challengeeID = p.getValue().getKey();
        String challenger = api.getClientInfo(challengerID).getNickname();
        String challengee = api.getClientInfo(challengeeID).getNickname();
        String[] p1Play = null;
        String[] p2Play = null;
        String res = "\n\n";
        switch (p.getKey().getValue()){
            case PLAYED_C:
                p1Play = scissors.split("\n");
                break;
            case PLAYED_R:
                p1Play = rock.split("\n");
                break;
            case PLAYED_P:
                p1Play = paper.split("\n");
                break;
        }
        switch (p.getValue().getValue()){
            case PLAYED_C:
                p2Play = scissors.split("\n");
                break;
            case PLAYED_R:
                p2Play = rock.split("\n");
                break;
            case PLAYED_P:
                p2Play = paper.split("\n");
                break;
        }
        assert p1Play != null;
        assert p2Play != null;
        for (int i = 0; i<p1Play.length; i++){
            res += p1Play[i]+"     "+p2Play[i]+"\n";
        }
        int winner;
        res = res.replace(" ","\t");
        //res += challenger+"-"+p.getKey().getValue()+ " - "+p.getValue().getValue()+"-"+challengee;
        if (p.getKey().getValue()==p.getValue().getValue()){
            res += "DRAW !";
        } else {
            res +="Winner : ";
        }
        if (p.getKey().getValue()==STATE_CHALLENGE.PLAYED_C && p.getValue().getValue()==STATE_CHALLENGE.PLAYED_P){
            res+=challenger;
        }
        if (p.getKey().getValue()==STATE_CHALLENGE.PLAYED_C && p.getValue().getValue()==STATE_CHALLENGE.PLAYED_R){
            res+=challengee;
        }
        if (p.getKey().getValue()==STATE_CHALLENGE.PLAYED_P && p.getValue().getValue()==STATE_CHALLENGE.PLAYED_C){
            res+=challengee;
        }
        if (p.getKey().getValue()==STATE_CHALLENGE.PLAYED_P && p.getValue().getValue()==STATE_CHALLENGE.PLAYED_R){
            res+=challenger;
        }
        if (p.getKey().getValue()==STATE_CHALLENGE.PLAYED_R && p.getValue().getValue()==STATE_CHALLENGE.PLAYED_P){
            res+=challengee;
        }
        if (p.getKey().getValue()==STATE_CHALLENGE.PLAYED_R && p.getValue().getValue()==STATE_CHALLENGE.PLAYED_C){
            res+=challenger;
        }
        System.out.println(res);
        api.sendChannelMessage(res);
    } //TODO .replace(" ","[color=white]\[/color]")


    public void changeName(Song song){
        api.renameServerGroup(groupID, song.getTitle().substring(0,song.getTitle().lastIndexOf("."))+" ♪♪");
    }

    private void toCalle(){
        changeName(player.getPlaying());
    }
}