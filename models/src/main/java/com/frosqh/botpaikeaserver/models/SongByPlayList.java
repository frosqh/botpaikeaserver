package com.frosqh.botpaikeaserver.models;

public class SongByPlayList extends AbstractModel {

    private Song song;
    private PlayList playList;

    public SongByPlayList(int id, Song s, PlayList p){
        super(id);
        song = s;
        playList = p;
    }
}
