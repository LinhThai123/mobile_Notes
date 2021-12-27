package com.example.ghichu.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ghichu.DTO.Category;
import com.example.ghichu.Database.Mydatabase;

import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    SQLiteDatabase database;

    // Call the database and perform data retrieval
    public CategoryDAO (Context context) {
        Mydatabase mydatabase = new Mydatabase(context);
        database = mydatabase.Open();
    }

    // create insert Category
    public boolean insertCategory (Category category){
        ContentValues values = new ContentValues() ;
        values.put("title" , category.getTitle());
        values.put("color" , category.getColor()) ;
        long index_insert = database.insert(Mydatabase.TABLE_CATEGORY , null , values);
        return index_insert >= 1 ;
    }

    // Show all list category
    public List<Category> getAllCategory() {
        List<Category> categoryList = new ArrayList<Category>() ;
        String sql = " SELECT * FROM " + Mydatabase.TABLE_CATEGORY ;
        Cursor cursor = database.rawQuery(sql,null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(0) ;
            String title = cursor.getString(1);
            String color = cursor.getString(2);
            Category category = new Category(id , title , color ) ;

            categoryList.add(category) ;
        }

        return categoryList ;
    }
    // update category follow id
    public boolean updateCategory (Category category){
        ContentValues values = new ContentValues() ;
        values.put("title",category.getTitle());
        values.put("color",category.getColor());

        String whereClause = "id = ?" ;
        String[] whereArgs = {String.valueOf(category.getId())};
        int index_update = database.update(Mydatabase.TABLE_CATEGORY , values , whereClause , whereArgs);
        return index_update >= -1 ;
    }

    // delete category from id
    public boolean deleteCategory (Category category) {
        String[] whereArgs = {String.valueOf(category.getId())};
        long index_delete = database.delete(Mydatabase.TABLE_CATEGORY , " id = ? " , whereArgs) ;
        return index_delete >= 1 ;
    }
}
