package com.surakshamitra.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.surakshamitra.model;

import java.util.ArrayList;

public class DBhelper extends SQLiteOpenHelper {
    private static final String dbname="contacts";
    public static final int version = 1;
    public DBhelper(Context context) {
        super(context, dbname, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String qry = "create table contact(id integer primary key autoincrement,name text,contact text)";
        db.execSQL(qry);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String qry = "DROP TABLE IF EXISTS contact";
        db.execSQL(qry);
        onCreate(db);
    }

    public boolean add_record(String name, String contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("contact", contact);

        long res = db.insert("contact", null, cv);
        db.close();

        return res != -1;
    }

    public Cursor readAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String qry = "select * from contact";
        Cursor cursor = db.rawQuery(qry, null);
        return cursor;
    }

    public boolean updateContact(String id, String name, String contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("name", name);
        contentValues.put("contact",contact); // Adjust the column name if needed

        int rowsUpdated = db.update("contact", contentValues, "id = ?", new String[]{id});
        db.close();

        Log.d("UpdateDebug", "Rows updated: " + rowsUpdated);

        return rowsUpdated > 0;
    }


    public boolean delete_data(String name, String number) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the contact exists
        Cursor cursor = db.rawQuery("SELECT * FROM contact WHERE name=? AND contact=?", new String[]{name, number});

        if (cursor.getCount() > 0) {
            // The contact exists, proceed with deletion
            long r = db.delete("contact", "name=? AND contact=?", new String[]{name, number});

            db.close(); // Close the database after the operation.

            return r != -1;
        } else {

            db.close();
            return false;
        }
    }

    public ArrayList<model> getAllContactsForSMS() {
        ArrayList<model> contactsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {"name", "contact"};

        Cursor cursor = db.query("contact", projection, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int nameIndex = cursor.getColumnIndex("name");
            int contactIndex = cursor.getColumnIndex("contact");

            String name = cursor.getString(nameIndex);
            String contact = cursor.getString(contactIndex);

            model contactModel = new model(name, contact);
            contactsList.add(contactModel);
        }

        cursor.close();
        db.close();

        return contactsList;
    }


}
