package com.frosqh.botpaikeaserver.ts3api;

import com.frosqh.botpaikeaserver.locale.FRFR;
import com.frosqh.botpaikeaserver.locale.Locale;
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
    private TS3Api ts3Api;
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
        ts3Api = ts3Query.getApi();
        ts3Api.login(login,password);
        ts3Api.selectVirtualServerById(1);
        ts3Api.setNickname(botName);

        goToRightChannel();

        final int clientId = ts3Api.whoAmI().getId();

        ts3Api.registerEvent(TS3EventType.TEXT_CHANNEL);
        ts3Api.registerEvent(TS3EventType.TEXT_PRIVATE);
        ts3Api.addTS3Listeners(new OnReceiveListener(clientId));

        welcomeAll();
    }

    private void goToRightChannel(){
        for (String user : knownUsers){
            Client tsClient = ts3Api.getClientByNameExact(user, true);
            if (tsClient != null && ts3Api.whoAmI().getChannelId() != tsClient.getChannelId()){
                ts3Api.moveQuery(tsClient.getChannelId());
                return;
            }
        }
    }

    private void welcomeAll(){
        for (String user : knownUsers){
            Client tsClient = ts3Api.getClientByNameExact(user, true);
            if (tsClient != null && !tsClient.isOutputMuted() && !tsClient.isAway()){
                ts3Api.sendPrivateMessage(tsClient.getId(),locale.welcomeMessage());
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

                if (commandManager.isEaster(command)){
                    ans = "Oui, c'est bien un easter que tu as là ! :P";
                }

                if (ans != null){
                    ts3Api.sendPrivateMessage(id, ans);
                } else {
                    ts3Api.sendPrivateMessage(id, locale.notFound(command));
                }
            }
        }
    }


}