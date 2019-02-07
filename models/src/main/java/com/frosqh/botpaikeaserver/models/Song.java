package com.frosqh.botpaikeaserver.models;

public class Song extends AbstractModel{

    private String title;
    private String artist;
    private String localurl;
    private String weburl;

    public Song(int id, String title, String artist, String localurl, String weburl){
        super(id);
        this.title = title;
        this.artist = artist;
        this.localurl = localurl;
        this.weburl = weburl;
    }

    @Override
    public String toString(){
        return artist+" - "+title;
    }
}
