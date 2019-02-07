package com.frosqh.botpaikeaserver.models;

public class User extends AbstractModel{

    private String username;
    private String password;
    private String mail;
    private String ytprofile;
    private String spprofile;
    private String deprofile;
    private String scprofile;

    public User(int id, String username, String password, String mail, String ytprofile, String spprofile, String deprofile, String scprofile){
        super(id);
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.ytprofile = ytprofile;
        this.spprofile = spprofile;
        this.deprofile = deprofile;
        this.scprofile = scprofile;
    }

    @Override
    public String toString(){
        return "User : "+username+" at "+mail;
    }
}
