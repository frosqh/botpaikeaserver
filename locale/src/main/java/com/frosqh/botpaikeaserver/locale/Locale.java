package com.frosqh.botpaikeaserver.locale;

public interface Locale{

    String paikeSong();

    String welcomeMessage();

    String list();

    String seeMore();

    // Commands Usage

    String usagehelp();
    String usagepaikea();
    String usagenext();
    String usageplay();
    String usagepause();
    String usageprev();
    String usagesetvolume();
    String usagetoggleautoplay();
    String usageinfo();
    String usagechallenge();

    // Commands Help

    String deschelp();
    String descpaikea();
    String descnext();
    String descplay();
    String descpause();
    String descprev();
    String descsetvolume();
    String desctoggleautoplay();
    String descinfo();
    String descchallenge();

    // Commands Results

    String nowPlaying(String song, String artist);
    String nothingPlaying();
    String succPlay();
    String toggleAutoPlayOn();
    String toggleAutoPlayOff();
    String challengeAnnounce(String challenger, String challengee);
    String challengeChoice();
    String challengeAsking();
    String challengeAbort();

    // Other

    String didYouMean(String arg, String almost);

    //Errors

    String errorPause();
    String errorOnPlay();
    String errorPlay();
    String notFound(String cmd);
    String undefinedBehavior();
    String easterUndefinedBehavior();
    String wip();
    String noPrev();

    // Easters input

    String easterShit();

    //Easters output

    String easterShitResponse();
    String easterGoogleResponse();
    String easterNoResponse();
    String easterPlopResponse();



}
