package com.example.ghichu.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ghichu.DTO.Category;

public class Mydatabase extends SQLiteOpenHelper {
    // create database
    private static final String DB_NAME = "notes.sqlite";

    // create table in database
    public static final String TABLE_CATEGORY = "tbl_category";
    public static final String TABLE_NOTE = "tbl_note";

    // create context
    private Context context;

    // contructor database
    public Mydatabase(Context context) {
        super(context, DB_NAME, null, 3);
    }

    @Override
    // create database
    public void onCreate(SQLiteDatabase db) {
        // create table Category
        String sqlCreateTableCategory = "CREATE TABLE " + TABLE_CATEGORY + "(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, color TEXT)";
        db.execSQL(sqlCreateTableCategory);
        ContentValues values = new ContentValues();
        // create 1 new element
        Category category = new Category(0, "All", "");
        values.put("title", category.getTitle());
        values.put("color", category.getColor());
        db.insert(TABLE_CATEGORY, null, values);

        // create table Notes
        String sqlCreateTableNote = "CREATE TABLE  " + TABLE_NOTE + "(id INTEGER PRIMARY KEY AUTOINCREMENT, id_category INTEGER, title TEXT, content TEXT, color TEXT, created_at TEXT, updated_at TEXT, " +
                "FOREIGN KEY (id_category) REFERENCES " + TABLE_CATEGORY + "(id))";
        db.execSQL(sqlCreateTableNote);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    // Show database (read and write database)
    public SQLiteDatabase Open() {
        return this.getWritableDatabase();
    }
}
