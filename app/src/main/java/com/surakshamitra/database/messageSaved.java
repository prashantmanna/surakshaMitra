package com.surakshamitra.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class messageSaved extends SQLiteOpenHelper {

    private static final String dbname = "message_database";
    private static final int version = 1;
    public messageSaved(Context context) {
        super(context, dbname, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String qry = "create table messages(id integer primary key autoincrement,message text)";
        db.execSQL(qry);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String qry = "drop table if exists messages";
        db.execSQL(qry);
        onCreate(db);

    }
    public boolean addMessage(String message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("message", message);

        long res = db.insert("messages", null, cv);
        db.close();

        return res != -1;
    }
    public ArrayList<String> getAllMessages() {
        ArrayList<String> messageList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        String[] messageProjection = {"message"};
        Cursor cursor = db.query("messages", messageProjection, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int msgIndex = cursor.getColumnIndex("message"); // Correct column name
                String msg = cursor.getString(msgIndex);
                messageList.add(msg);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();

        return messageList;
    }
}
