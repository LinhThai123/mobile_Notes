package com.example.ghichu.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ghichu.DTO.Notes;
import com.example.ghichu.Database.Mydatabase;

import java.util.ArrayList;
import java.util.List;

public class NotesDAO {

    SQLiteDatabase database;

    // Call the database and perform data retrieval
    public NotesDAO(Context context) {
        Mydatabase mydatabase = new Mydatabase(context);
        database = mydatabase.Open();
    }

    // show all list category
    public List<Notes> getAllNote() {
        List<Notes> notesList = new ArrayList<>();
        String sql = "SELECT * FROM " + Mydatabase.TABLE_NOTE;
        Cursor cursor = database.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            int category_id = cursor.getInt(1);
            String title = cursor.getString(2);
            String content = cursor.getString(3);
            String color = cursor.getString(4);
            String create_at = cursor.getString(5);
            String update_at = cursor.getString(6);

            // create new notes
            Notes notes = new Notes(id, category_id, title, content, color, create_at, update_at);
            notesList.add(notes); // add new notes
        }
        return notesList;
    }

    // insert new notes
    public boolean insertNote(Notes notes) {
        ContentValues values = new ContentValues();
        values.put("id_category", notes.getCategory_id());
        values.put("title", notes.getTitle());
        values.put("content", notes.getContent());
        values.put("color", notes.getColor());
        values.put("created_at", notes.getCreateAt());
        values.put("updated_at", notes.getUpdateAt());

        long check = database.insert(Mydatabase.TABLE_NOTE, null, values);
        return check >= 1;
    }

    // update notes follow id
    public boolean updateNote(Notes notes) {
        ContentValues values = new ContentValues();
        values.put("id_category", notes.getCategory_id());
        values.put("title", notes.getTitle());
        values.put("content", notes.getContent());
        values.put("color", notes.getColor());
        values.put("created_at", notes.getCreateAt());
        values.put("updated_at", notes.getUpdateAt());

        String whereClause = "id = ? ";
        String[] whereArgs = {String.valueOf(notes.getId())};
        long check = database.update(Mydatabase.TABLE_NOTE, values, whereClause, whereArgs);
        return check >= 1;
    }

    // delete note follow id
    public boolean deleteNote(Notes notes) {
        String[] whereArgs = {String.valueOf(notes.getId())};
        long index_delete = database.delete(Mydatabase.TABLE_CATEGORY, " id = ? ", whereArgs);
        return index_delete >= 1;
    }

    // Sort Note follow update_at (Ngày mới nhất )
    public List<Notes> sortDate(String type) {//desc hoạc asc

        List<Notes> notesList = new ArrayList<>();
        String sql = "SELECT * FROM " + Mydatabase.TABLE_NOTE + " ORDER BY updated_at " + type;
        Cursor cursor = database.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            int category_id = cursor.getInt(1);
            String title = cursor.getString(2);
            String content = cursor.getString(3);
            String color = cursor.getString(4);
            String created_at = cursor.getString(5);
            String updated_at = cursor.getString(6);

            Notes notes = new Notes(id, category_id, title, content, color, created_at, updated_at);
            notesList.add(notes);
        }
        return notesList;
    }

    // Sort Note follow update_at (Ngày cũ nhất )
    public List<Notes> sortTitle(String type) {
        List<Notes> notesList = new ArrayList<>();
        String sql = "SELECT * FROM " + Mydatabase.TABLE_NOTE + " ORDER BY title " + type ;
        Cursor cursor = database.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            int category_id = cursor.getInt(1);
            String title = cursor.getString(2);
            String content = cursor.getString(3);
            String color = cursor.getString(4);
            String created_at = cursor.getString(5);
            String updated_at = cursor.getString(6);

            Notes notes = new Notes(id, category_id, title, content, color, created_at, updated_at);
            notesList.add(notes);
        }
        return notesList;
    }

    // show Note by category id
    public List<Notes> getNoteByCategoryId(int categoryId) {
        List<Notes> notesList = new ArrayList<Notes>();
        String sql = " SELECT * FROM " + Mydatabase.TABLE_NOTE + " WHERE id_category = " + categoryId;
        Cursor cursor = database.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            int id_category = cursor.getInt(1);
            String title = cursor.getString(2);
            String content = cursor.getString(3);
            String color = cursor.getString(4);
            String create_at = cursor.getString(5);
            String update_at = cursor.getString(6);

            Notes notes = new Notes(id, id_category, title, content, color, create_at, update_at);
            notesList.add(notes);
        }
        return notesList;
    }

    public List<Notes> findNoteByTitle(String titleSearch) {
        List<Notes> notesList = new ArrayList<Notes>();
        String sql = " SELECT * FROM " + Mydatabase.TABLE_NOTE + " WHERE title like '%" + titleSearch + "%'";
        Cursor cursor = database.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            int id_category = cursor.getInt(1);
            String title = cursor.getString(2);
            String content = cursor.getString(3);
            String color = cursor.getString(4);
            String create_at = cursor.getString(5);
            String update_at = cursor.getString(6);

            Notes notes = new Notes(id, id_category, title, content, color, create_at, update_at);
            notesList.add(notes);
        }
        return notesList;
    }
    public List<Notes> findNoteByData (String startDate , String endDate) {
        List<Notes> notesList = new ArrayList<Notes>();
        String sql = "SELECT * FROM " + Mydatabase.TABLE_NOTE + " WHERE updated_at BETWEEN '" + startDate + "' AND '" + endDate + "'";
        Cursor cursor = database.rawQuery(sql,null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            int id_category = cursor.getInt(1);
            String title = cursor.getString(2);
            String content = cursor.getString(3);
            String color = cursor.getString(4);
            String create_at = cursor.getString(5);
            String update_at = cursor.getString(6);

            Notes notes = new Notes(id, id_category, title, content, color, create_at, update_at);
            notesList.add(notes);
        }
        return notesList ;

    }
}
