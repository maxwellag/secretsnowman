package com.secretsnowman.secretsnowman.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.secretsnowman.secretsnowman.Entity.User;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "users.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(User.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE_NAME);
        onCreate(db);
    }

    public long insertUser(User userToInsert){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // We don't care about the localId, since it is auto-assigned.
        // ServerId will be updated after making a request to the server to get the serverId
        values.put(User.COLUMN_NAME, userToInsert.getName());
        long id = db.insert(User.TABLE_NAME, null, values);

        db.close();

        return id;
    }

    public void deleteUser(User userToDelete){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(User.TABLE_NAME, User.COLUMN_ID + "=?",
                new String[]{String.valueOf(userToDelete.getLocalId())});
        db.close();
    }

    public User getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(User.TABLE_NAME, new String[]{
                        User.COLUMN_ID, User.COLUMN_NAME},
                User.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
        }
        else{
            return null;
        }


        User u = new User(cursor.getInt(cursor.getColumnIndex((User.COLUMN_ID))),
                cursor.getString(cursor.getColumnIndex((User.COLUMN_NAME))),
                -1);
        cursor.close();
        return u;
    }

    public int updateUser(User userToUpdate){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(User.COLUMN_SERVERID, userToUpdate.getServerId());
        // More can be added here to be updated in the future.
        return db.update(User.TABLE_NAME, values, User.COLUMN_ID + "=?",
                new String[]{String.valueOf(userToUpdate.getLocalId())});
    }
}
