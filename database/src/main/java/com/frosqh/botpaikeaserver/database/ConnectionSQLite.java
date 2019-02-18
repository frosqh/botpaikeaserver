package com.frosqh.botpaikeaserver.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionSQLite {

    private static String filename = "BotPaikea.db";

    private static String url = "jdbc:sqlite:";

    private static Connection connect;

    public static Connection getInstance(){
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e){
            e.printStackTrace();
            //TODO HandleError
        }
        if (connect == null) {
            try {
                connect = DriverManager.getConnection(url+filename);
            } catch (SQLException e) {
                e.printStackTrace();
                //TODO HandleError
            }
        }
        return connect;
    }
}
