package com.frosqh.botpaikeaserver.models;

public class PlayList extends AbstractModel{

    private String name;
    private User creator_id;

    public PlayList(int id, String name, User creator_id){
        super(id);
        this.name = name;
        this.creator_id = creator_id;
    }

}
