package com.frosqh.botpaikeaserver.Locale;

public class FR_FR implements Locale {
    @Override
    public String paikeaSong() {
        return null;
    }

    @Override
    public String welcomeMessage() {
        return "Plop, je suis en ligne ! Tente !help pour avoir la liste des commandes disponibles.";
    }

    @Override
    public String list() {
        return "Voici la liste de commandes disponibles : \n";
    }

    @Override
    public String seeMore() {
        return "N'hésite pas à taper !help [cmd] pour obtenir de l'aide spécifique à une commande";
    }

    @Override
    public String usageHelp() {
        return "Usage : !help [cmd]";
    }

    @Override
    public String usagePaikea() {
        return "Usage : !paikea";
    }

    @Override
    public String usageNext() {
        return "Usage : !next";
    }

    @Override
    public String usagePlay() {
        return "Usage : !play";
    }

    @Override
    public String usagePause() {
        return "Usage : !pause";
    }

    @Override
    public String usagePrev() {
        return "Usage : !prev";
    }

    @Override
    public String usageSetVolume() {
        return "Usage : !setVolume [volume]";
    }

    @Override
    public String usageToggleAutoPlay() {
        return "Usage : !toggleAutoPlay";
    }

    @Override
    public String usageInfo() {
        return "Usage : !info";
    }

    @Override
    public String descHelp() {
        return "Affiche ce message d'aide";
    }

    @Override
    public String descPaikea() {
        return "Suprise !";
    }

    @Override
    public String descNext() {
        return "Lit la prochaine musique dans la liste d'attente";
    }

    @Override
    public String descPlay() {
        return "Sort le lecteur de l'état 'Pause'";
    }

    @Override
    public String descPause() {
        return "Met le lecteur dans l'état 'Pause'";
    }

    @Override
    public String descPrev() {
        return  "Revient à la dernière musique jouée";
    }

    @Override
    public String descSetVolume() {
        return "Change le volume du lecteur";
    }

    @Override
    public String descToggleAutoPlay() {
        return "(Dés)Active la lecture automatique";
    }

    @Override
    public String descInfo() {
        return "Affiche la musique en cours";
    }

    @Override
    public String nowPlaying(String song, String artist) {
        return  "♫ Now playing - "+song+" by " + artist + " ♫";
    }

    @Override
    public String nothingPlaying() {
        return "Aucune musique n'est en train d'être jouée.";
    }

    @Override
    public String succPlay() {
        return "La lecture a bien été (re)prise !";
    }

    @Override
    public String toggleAutoPlayOn() {
        return "La lecture automatique est désormais active !";
    }

    @Override
    public String toggleAutoPlayOff() {
        return "La lecture automatique est désactivée";
    }

    @Override
    public String didYouMean(String arg, String almost) {
        return  "Désolé, mais la commande "+arg+" n'existe pas. Vouliez-vous dire "+almost+" ?";
    }

    @Override
    public String errorPause() {
        return  "Rien n'est est en train d'être joué, réfléchis un peu ><";
    }

    @Override
    public String errorOnPlay() {
        return "La lecture n'a pas pu être reprise, n'hésite pas à réessayer si cela te semble étrange.";
    }

    @Override
    public String errorPlay() {
        return "Ça joue déjà, réfléchis un peu ><";
    }

    @Override
    public String notFound(String cmd) {
        return "Commande "+cmd+" non trouvée";
    }

    @Override
    public String undefinedBehavior() {
        return "Alors, crois le ou non, mais il semblerait qu'on est oublié de définir le fonctionnement de cette commande 0:)";
    }

    @Override
    public String easterUndefinedBehavior() {
        return "Ah, c'est un peu bête, je suis sûr qu'on avait pensé à un easter egg ici, mais on a du oublier :/";
    }

    @Override
    public String wip() {
        return "Cette fonction est encore en cours de développement, elle peut ne pas être fonctionnelle";
    }

    @Override
    public String noPrev() {
        return "Désolé, mais aucune chanson n'a précédé celle-ci";
    }

    @Override
    public String easterShit() {
        return "merde";
    }

    @Override
    public String easterShitResponse() {
        return "Diantre*";
    }

    @Override
    public String easterGoogleResponse() {
        return "Nan mais oh, tu me prends pour qui là ? ><";
    }

    @Override
    public String easterNoResponse() {
        return "Si.";
    }

    @Override
    public String easterPlopResponse() {
        return "Plop à toi, mon frère !";
    }
}
