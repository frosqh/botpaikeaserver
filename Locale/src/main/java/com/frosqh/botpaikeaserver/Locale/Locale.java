package com.frosqh.botpaikeaserver.Locale;

public interface Locale{

    String PAIKEA_SONG();

    String WELCOME_MESSAGE();

    String LIST();

    String SEE_MORE();

    // Commands Usage

    String USAGE_HELP();
    String USAGE_PAIKEA();
    String USAGE_NEXT();
    String USAGE_PLAY();
    String USAGE_PAUSE();
    String USAGE_PREV();
    String USAGE_SETVOLUME();
    String USAGE_TOGGLEAUTOPLAY();
    String USAGE_INFO();

    // Commands Help

    String DESC_HELP();
    String DESC_PAIKEA();
    String DESC_NEXT();
    String DESC_PLAY();
    String DESC_PAUSE();
    String DESC_PREV();
    String DESC_SETVOLUME();
    String DESC_TOGGLEAUTOPLAY();
    String DESC_INFO();

    // Commands Results

    String NOW_PLAYING(String song, String artist);
    String NOTHING_PLAYING();
    String SUCC_PLAY();
    String TOGGLE_AUTOPLAY_ON();
    String TOGGLE_AUTOPLAY_OFF();

    // Other

    String DID_YOU_MEAN();

    //Errors

    String ERROR_PAUSE();
    String ERROR_ON_PLAY();
    String ERROR_PLAY();
    String NOT_FOUND(String cmd);
    String UNDEFINED_BEHAVIOR();
    String EASTER_UNDEFINED_BEHAVIOR();
    String WIP();
    String NO_PREV();

    // Easters input

    String EASTER_SHIT();

    //Easters output

    String EASTER_SHIT_RESPONSE();
    String EASTER_GOOGLE_RESPONSE();
    String EASTER_NO_RESPONSE();
    String EASTER_PLOP_RESPONSE();



}
