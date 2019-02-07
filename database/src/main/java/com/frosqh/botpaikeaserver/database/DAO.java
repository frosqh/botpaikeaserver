package com.frosqh.botpaikeaserver.database;

import com.frosqh.botpaikeaserver.models.AbstractModel;

import java.sql.Connection;
import java.util.List;

public abstract class DAO<T extends AbstractModel> {

    Connection connect = ConnectionSQLite.getInstance();

    protected abstract String getTableName();

    public abstract T find(int id);

    public abstract T create(T obj);

    public abstract T update(T obj);

    public abstract void delete(T obj);

    public abstract List<T> getList();

    public abstract List<T> filter(String... args);

    protected String getFindRequest(int id){
        return "SELECT * FROM "+getTableName()+" WHERE id = "+id;
    }

    protected String getMaxRequest(){
        return "SELECT MAX(id) FROM "+getTableName();
    }

    protected String getDeleteRequest(int id){
        return "DELETE FROM "+getTableName()+" WHERE id = "+id;
    }

    protected String getDeleteForeignRequest(String tableName, int id){
        return "DELETE FROM "+tableName+" WHERE "+getTableName()+"_id = "+id;
    }

    protected String getUpdateRequest(String upd, int id){
        return "UPDATE "+getTableName()+" SET "+upd+" WHERE id = "+id;
    }
}
