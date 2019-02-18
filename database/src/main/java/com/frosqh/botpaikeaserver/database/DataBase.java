package com.frosqh.botpaikeaserver.database;

import com.frosqh.botpaikeaserver.models.Song;
import com.frosqh.botpaikeaserver.settings.Settings;
import com.frosqh.botpaikeaserver.file_explorer.DiskFileExplorer;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataBase {

    private static final String TABLE_SONG = "title TEXT NOT NULL,\n"
            + "artist TEXT NOT NULL,\n"
            + "localurl TEXT,\n"
            + "weburl TEXT";

    private static final String TABLE_USER = "username TEXT NOT NULL,\n"
            + "password TEXT,\n"
            + "mail TEXT NOT NULL,\n"
            + "ytprofile TEXT,\n"
            + "spprofile TEXT,\n"
            + "deprofile TEXT,\n"
            + "scprofile TEXT";

    private static final String TABLE_PLAYLIST = "name TEXT NOT NULL,\n"
            + "creator_id INTEGER NOT NULL,\n"
            + "FOREIGN KEY (creator_id) REFERENCES user(id)";

    private static final String TABLE_SONGBYPLAYLIST = "song_id INTEGER NOT NULL,\n"
            + "playlist_id INTEGER NOT NULL,\n"
            + "FOREIGN KEY (song_id) REFERENCES song(id),\n"
            + "FOREIGN KEY (playlist_id) REFERENCES playlist(id)";

    public DataBase(){
        File db = new File(Settings.getInstance().get("database")); //TODO Replace by Settings
        if (!db.exists()){
            Connection connect = ConnectionSQLite.getInstance();

            String createSong = createTable("song",TABLE_SONG);
            String createUser = createTable("user",TABLE_USER);
            String createPlayList = createTable("playlist", TABLE_PLAYLIST);
            String createSongByPlayList = createTable("songbyplaylist",TABLE_SONGBYPLAYLIST);

            Statement stm = null;

            try {
                stm = connect.createStatement();
            } catch (SQLException e) {
                //TODO Error Handler
            }

            assert stm != null;
            //TODO Logging

            try {
                stm.execute(createSong);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                stm.execute(createUser);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                stm.execute(createPlayList);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                stm.execute(createSongByPlayList);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            //TODO Logging
        }
    }

    private String createTable(String name, String Table){
        return "CREATE TABLE IF NOT EXISTS " + name + "(\n"
                +"id INTEGER PRIMARY KEY, \n"
                + Table + "\n);";
    }

    public void refreshSongs(){
        String[] files = Settings.getInstance().get("dirs").split(";");
        SongDAO songDAO =  new SongDAO();
        for (String f : files){
            DiskFileExplorer dfe = new DiskFileExplorer(f, true);
            List<Song> songs = songDAO.getList();
            List<String> paths = dfe.list();

            for (Song s : songs){
                String URL1 = s.getLocalurl();
                String URL = URL1.replace(f,"");
                URL = URL.substring(1);
                if (!paths.contains(URL)) songDAO.delete(s);
                else paths.remove(URL);
            }

            LocalDateTime now = LocalDateTime.now();
            if (paths.size()>0){
                for (String p : paths){
                    if (p.contains("_-_")) {
                        String s = p.substring(p.indexOf("_-_")+3).replace("_"," ");
                        String t = p.substring(0,p.indexOf("_-_")).replace("_"," ");
                        Song temp = new Song(-1, s, t, f+"\\"+p,null);
                        songDAO.create(temp);
                    } else {
                        Song temp = new Song(-1, p, "unknown", f+"\\"+p,null);
                        songDAO.create(temp);
                    }
                }
            }
            LocalDateTime then = LocalDateTime.now();
            System.out.println("Chargement des chansons : \n" + ChronoUnit.SECONDS.between(now,then) + "s");
        }
    }
}
