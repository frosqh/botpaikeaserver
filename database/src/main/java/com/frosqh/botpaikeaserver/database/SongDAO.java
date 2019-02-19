package com.frosqh.botpaikeaserver.database;

import com.frosqh.botpaikeaserver.models.Song;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SongDAO extends DAO<Song> {

    @Override
    protected String getTableName() {
        return "song";
    }

    @Override
    public Song find(int id) {
        try (Statement stm = connect.createStatement()) {
            String select = getFindRequest(id);
            try (ResultSet result = stm.executeQuery(select)) {
                String title = result.getString("title");
                String artist = result.getString("artist");
                String localurl = result.getString("localurl");
                String weburl = result.getString("weburl");
                Song song = new Song(id, title, artist, localurl, weburl);
                stm.close();
                return song;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Song create(Song obj) {
        try (Statement stm = connect.createStatement()){
            String select = getMaxRequest();

            int id = 1;
            try (ResultSet result = stm.executeQuery(select)){
                if (result.next())
                    id = result.getInt(1)+ 1;
                stm.close();
            } catch (SQLException e){
                e.printStackTrace();
            }

            try  (PreparedStatement prepare = connect.prepareStatement("INSERT INTO song (id, title, artist, localurl, weburl) VALUES (?,?,?,?,?)")) {
                prepare.setString(2, obj.getTitle());
                prepare.setString(3, obj.getArtist());
                if (obj.getLocalurl() != null)
                    prepare.setString(4, obj.getLocalurl());
                if (obj.getWeburl() != null)
                    prepare.setString(5, obj.getWeburl());
                prepare.executeUpdate();
                prepare.close();
                return find(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Song update(Song obj) {
        try (Statement stm = connect.createStatement()){
            String upd = "title = '"+obj.getTitle()+"', "
                    + "artist = '"+obj.getArtist()+"', "
                    + "localurl = '"+obj.getLocalurl()+"', "
                    + "weburl = '"+obj.getWeburl()+"'";
            upd = getUpdateRequest(upd, obj.getId());
            stm.executeUpdate(upd);
            obj = find(obj.getId());
            stm.close();
            return obj;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(Song obj) {
        try (Statement stm = connect.createStatement()){
            String del = getDeleteRequest(obj.getId());
            stm.executeUpdate(del);
            del = getDeleteForeignRequest("songbyplaylist",obj.getId());
            stm.executeUpdate(del);
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Song> getList() {
        List<Song> list = new ArrayList<>();
        try (Statement stm = connect.createStatement()){
            String request = "SELECT id FROM "+getTableName();
            try (ResultSet res = stm.executeQuery(request)) {
                while (res.next())
                    list.add(find(res.getInt("id")));
                stm.close();
                return list;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Song> filter(String... args) {
        String where;
        {
            StringBuilder whereB = new StringBuilder();
            boolean b = true;
            for (String s : args){
                whereB.append(!b?"'":"").append(s).append(!b?"'":"").append(b?"=":" AND ");
                b=!b;
            }
            where = whereB.toString().substring(0,whereB.lastIndexOf("A")-1);
        }
        List<Song> list = new ArrayList<>();
        try (Statement stm = connect.createStatement()){
            String request = "SELECT id FROM "+getTableName()+" WHERE "+where;
            try (ResultSet result = stm.executeQuery(request)) {
                while (result.next())
                    list.add(find(result.getInt("id")));
                stm.close();
                return list;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
