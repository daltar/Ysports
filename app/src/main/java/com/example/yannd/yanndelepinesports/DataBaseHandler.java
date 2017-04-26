
package com.example.yannd.yanndelepinesports;


import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;



/**
 * Created by yannd on 25/04/2017.
 */

public class DataBaseHandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Ysport.db";

    public static final String ARTICLE_TABLE_NAME = "article_table";

    public static final String ARTICLE_KEY = "ID";
    public static final String ARTICLE_AUTHOR = "AUTHOR";
    public static final String ARTICLE_TITLE = "TITTLE";
    public static final String ARTICLE_DESCRIPTION = "DESCRIPTION";
    public static final String ARTICLE_URL = "URL";
    public static final String ARTICLE_URLTOIMAGE = "URLTOIMAGE";
    public static final String ARTICLE_PUBLISHEDATE = "PUBLISHEDATE";




    public static final String ARTICLE_TABLE_CREATE =
            "CREATE TABLE " + ARTICLE_TABLE_NAME + " (" +
                    ARTICLE_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ARTICLE_AUTHOR + " TEXT, " +
                    ARTICLE_TITLE + " TEXT, " +
                    ARTICLE_DESCRIPTION + " TEXT, " +
                    ARTICLE_URL + " TEXT, " +
                    ARTICLE_URLTOIMAGE + " TEXT, " +
                    ARTICLE_PUBLISHEDATE + " TEXT);";

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ARTICLE_TABLE_CREATE);
    }

    public static final String ARTICLE_TABLE_DROP = "DROP TABLE IF EXISTS " + ARTICLE_TABLE_NAME + ";";

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ARTICLE_TABLE_DROP);
        onCreate(db);
    }

    public boolean insertData(ArticleModel articleModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ARTICLE_AUTHOR,articleModel.getAuthor());
        contentValues.put(ARTICLE_TITLE,articleModel.getTitle());
        contentValues.put(ARTICLE_DESCRIPTION,articleModel.getDescription());
        contentValues.put(ARTICLE_URL,articleModel.getUrl());
        contentValues.put(ARTICLE_URLTOIMAGE,articleModel.getUrlToImage());
        contentValues.put(ARTICLE_PUBLISHEDATE,articleModel.getPublishedAt());
        long result = db.insert(ARTICLE_TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ARTICLE_TABLE_NAME,null);
        return res;
    }

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(ARTICLE_TABLE_NAME, "ID = ?",new String[] {id});
    }

}
