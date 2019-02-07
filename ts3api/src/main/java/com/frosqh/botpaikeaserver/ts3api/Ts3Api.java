package com.frosqh.botpaikeaserver.ts3api;

import com.frosqh.botpaikeaserver.locale.FRFR;
import com.frosqh.botpaikeaserver.locale.Locale;
import com.frosqh.botpaikeaserver.ts3api.exception.NotACommandException;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ts3Api {
    private TS3Api api;
    private List<String> knownUsers;
    private Locale locale;
    private CommandManager commandManager;

    public Ts3Api(String host, String login, String password, String botName, List<String> users, Locale locale){
        knownUsers = users;
        this.locale = locale;
        commandManager = new CommandManager(this.locale);

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
                api.sendPrivateMessage(tsClient.getId(),locale.welcomeMessage());
            }
        }
    }

    public static void main(String[] args){
        new Ts3Api("harinman.ddns.net","BotPaikea","d9DmuZnq","Bot Paikea",new ArrayList<String>(Collections.singleton("frosqh")),new FRFR());
    }

    private class OnReceiveListener extends TS3EventAdapter {
        public final int selfID;

        public OnReceiveListener(int self) {
            selfID = self;
        }

        @Override
        public void onTextMessage(TextMessageEvent e){
            if (e.getTargetMode()== TextMessageTargetMode.CHANNEL && e.getInvokerId()!=selfID){
                //Channel message, check for !invoke
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
                        else if (commandManager.isComplex(command))
                            ans = commandManager.execComplex(command);
                    } catch (NotACommandException ignored) {}
                }


                if (ans != null){
                    api.sendPrivateMessage(id, ans);
                } else {
                    api.sendPrivateMessage(id, locale.notFound(command));
                }
            }
        }
    }


}