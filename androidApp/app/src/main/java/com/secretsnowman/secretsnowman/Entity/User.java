package com.secretsnowman.secretsnowman.Entity;

public class User {
    public static final String TABLE_NAME = "users";
    public static final String COLUMN_ID = "localId";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SERVERID = "serverId";
    private int localId;
    private String name;
    private int serverId;

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT, "
                    + COLUMN_SERVERID + " INTEGER"
                    + ")";

    public User(int localId, String name, int serverId){
        this.localId = localId;
        this.name = name;
        this.serverId = serverId;
    }

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }
}
