package com.frosqh.botpaikeaserver.player;

import com.frosqh.botpaikeaserver.database.SongDAO;
import com.frosqh.botpaikeaserver.models.Song;
import com.frosqh.botpaikeaserver.locale.Locale;
import com.frosqh.botpaikeaserver.player.exceptions.EmptyHistoryException;
import com.frosqh.botpaikeaserver.player.exceptions.PauseException;
import com.frosqh.botpaikeaserver.player.exceptions.PlayException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;

public class Player {

    private MediaPlayer mediaPlayer;
    private final Stack<Song> history;
    private Deque<Song> queue;
    private double volume = 0.5;
    private boolean autoPlay = true;
    private boolean isPlaying = false;
    private Locale locale;
    private MethodCall method;

    @FunctionalInterface
    public interface MethodCall {
        void call();
    }

    public Player(Locale locale){
        history = new Stack<>();
        queue = new ArrayDeque<>();
        this.locale = locale;
    }


    public double getVolume(){
        return volume;
    }

    public void setVolume(double vol){
        volume = vol/100;
        mediaPlayer.setVolume(volume);
    }

    public Song getPlaying(){
        if (isPlaying)
            return queue.peek();
        else
            return null;
    }

    public void add(Song newSong){
        queue.add(newSong);
    }

    public double getDuration(){
        return mediaPlayer.getTotalDuration().toMillis();
    }

    public double getTimeCode(){
        return mediaPlayer.getCurrentTime().toMillis();
    }

    private void initPlayer(){
        mediaPlayer.setVolume(volume);
        if (autoPlay) mediaPlayer.setOnEndOfMedia(this::next);
        else mediaPlayer.setOnEndOfMedia(null);
    }

    public void next() {
        if (mediaPlayer != null)
            mediaPlayer.stop();
        isPlaying = false;
        if (!queue.isEmpty())
            history.add(queue.poll());
        Song nextSong;
        if (queue.isEmpty()) { //Choose a random song in our song List
            SongDAO songDAO = new SongDAO();
            List<Song> songs = songDAO.getList();
            int rand = (int) (Math.random() * (songs.size() - 1));
            nextSong = songs.get(rand);
            queue.add(nextSong);
        }
        nextSong = queue.peek();
        File f = new File(nextSong.getLocalurl());
        Media media = new Media(f.toURI().toString());
        if (mediaPlayer!=null)
            mediaPlayer.dispose();
        mediaPlayer = new MediaPlayer(media);
        initPlayer();
        mediaPlayer.play();
        isPlaying = true;
        method.call();

    }

    public void prev() throws EmptyHistoryException {
        if (history.isEmpty())
            throw new EmptyHistoryException();
        if (mediaPlayer!=null)
            mediaPlayer.stop();
        queue.addFirst(history.pop());
        Song nextSong = queue.peek();
        File f = new File(nextSong.getLocalurl());
        Media media = new Media(f.toURI().toString());
        if (mediaPlayer != null)
            mediaPlayer.dispose();
        mediaPlayer = new MediaPlayer(media);
        initPlayer();
        mediaPlayer.play();
        isPlaying = true;
    }

    public void play() throws PlayException {
        if (mediaPlayer==null || isPlaying)
            throw new PlayException();
        mediaPlayer.play();
        isPlaying = true;
    }

    public void pause() throws PauseException {
        if (mediaPlayer==null || !isPlaying)
            throw new PauseException();
        mediaPlayer.pause();
        isPlaying = false;
    }

    public void toggleAutoPlay(){
        if (!autoPlay)
            mediaPlayer.setOnEndOfMedia(this::next);
        else
            mediaPlayer.setOnEndOfMedia(null);
        autoPlay=!autoPlay;
    }

    public boolean isAutoPlay(){
        return autoPlay;
    }

    public String getInfos(){
        if (!isPlaying) return locale.nothingPlaying();
        Song songPl = getPlaying();
        int t = (int) getTimeCode()/1000;
        int v = (int) (getVolume()*100);
        int d = (int) getDuration()/1000;
        String current = "";
        int mC = t/60;
        int sC = t%60;
        if (mC>0)
            current = String.valueOf(mC)+"m";
        current += String.valueOf(sC)+"s";
        String total = "";
        int mD = d/60;
        int sD = d%60;
        if (mD>0)
            total = String.valueOf(mD)+"m";
        total += String.valueOf(sD)+"s";
        return locale.nowPlaying(songPl.getTitle(),songPl.getArtist())+"  ("+current+"/"+total+")"+"\n\t\tPlayer Volume : "+v;
    }


    public void setMethod(MethodCall toCalle) {
        this.method = toCalle;
    }
}
