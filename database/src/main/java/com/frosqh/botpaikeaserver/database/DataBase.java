package com.frosqh.botpaikeaserver.database;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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
            + "song_id INTEGER NOT NULL,\n"
            + "playlist_id INTEGER NOT NULL,\n"
            + "FOREIGN KEY (song_id) REFERENCES song(id),\n"
            + "FOREIGN KEY (playlist_id) REFERENCES playlist(id)";

    public DataBase(){
        File db = new File("settings.database"); //TODO Replace by Settings
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
        return "CREATE TABLE IF NOT EXISTS" + name + "(\n"
                + Table + "\n);";
    }
}
