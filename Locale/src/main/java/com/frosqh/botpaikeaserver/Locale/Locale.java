package com.frosqh.botpaikeaserver.Locale;

public interface Locale{

    String paikeaSong();

    String welcomeMessage();

    String list();

    String seeMore();

    // Commands Usage

    String usageHelp();
    String usagePaikea();
    String usageNext();
    String usagePlay();
    String usagePause();
    String usagePrev();
    String usageSetVolume();
    String usageToggleAutoPlay();
    String usageInfo();

    // Commands Help

    String descHelp();
    String descPaikea();
    String descNext();
    String descPlay();
    String descPause();
    String descPrev();
    String descSetVolume();
    String descToggleAutoPlay();
    String descInfo();

    // Commands Results

    String nowPlaying(String song, String artist);
    String nothingPlaying();
    String succPlay();
    String toggleAutoPlayOn();
    String toggleAutoPlayOff();

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
