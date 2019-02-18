package com.frosqh.botpaikeaserver.settings;

import com.frosqh.botpaikeaserver.locale.FRFR;
import com.frosqh.botpaikeaserver.locale.Locale;

import java.io.*;
import java.util.*;

public class Settings extends HashMap<String, String> {

    private Locale locale;
    private final static String[] keywords = {"database","dirs","port","sv_address","sv_login","sv_password"};
    private final static String[] intExp = {"port"};

    private static final String properties =
            "#Bot Paikea Server Properties\n" +
                    "database=BotPaikea.db\n" +
                    "dirs=C:\\Users\\Admin\\Music\n"+
                    "port=2302\n"+
                    "bot_name=Bot Paikea\n"+
                    "sv_address=127.0.0.1\n"+
                    "sv_login=login_quer\n"+
                    "sv_password=password_query\n"+
                    "known_users=john;james";

    private static Settings instance;

    public static Settings getInstance(){
        if (instance == null)
            instance = new Settings();
        return instance;
    }

    public void createSettings() throws Exception {
        FileWriter propertiesWriter = new FileWriter("./server.properties");
        BufferedWriter bufferedWriter = new BufferedWriter(propertiesWriter);
        bufferedWriter.write(properties);
        bufferedWriter.close();
        propertiesWriter.close();
        System.err.println("Settings à compléter");
        System.exit(1);
    }

    public void load() throws Exception {
        try {
            FileReader propertiesReader = new FileReader("./server.properties");
            BufferedReader bufferedReader = new BufferedReader(propertiesReader);
            String line;
            while ((line = bufferedReader.readLine())!=null){
                if (!line.startsWith("#")){
                    String[] prop = line.split("=");
                    String key = prop[0];
                    String value = prop[1];
                    if (Arrays.asList(intExp).contains(key) && !isInteger(value))
                        throw new Exception("Integer expected");
                    if (key.equals("locale")){
                        switch (value){
                            case "fr_fr":
                                locale = new FRFR();
                            default:
                                locale = new FRFR(); //TODO ENEN
                        }
                    } else put(key,value);
                }
            }
            bufferedReader.close();
            propertiesReader.close();
        } catch (FileNotFoundException e) {
            createSettings();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String missings = checkSettingsIntegrity();
        if (missings != null)
            throw new Exception(missings);
    }

    private boolean isInteger(String str){
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    private String checkSettingsIntegrity(){
        Set<String> keySet = keySet();
        List<String> errs = new ArrayList<>();
        for (String key : keywords)
            if (!keySet.contains(key))
                errs.add(key);
        StringBuilder res = new StringBuilder();
        for (String e : errs)
            res.append(e).append(", ");
        if (res.length()>0)
            return res.substring(0,res.length()-2);
        return null;
    }

    public Locale getLocale() {
        return locale;
    }
}
