package com.roy.bkapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 1vPy(Roy) on 2017/5/12.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "bkApp.db";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TB_COLLECTION = "create table if not exists tb_collection(" +
            "id INTEGER primary key autoincrement not null," +
            "imageUrl TEXT not null," +
            "movieName TEXT not null," +
            "movieId TEXT not null," +
            "isSync Integer not null);";
    private static DbHelper instance;

    private Context mContext;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TB_COLLECTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
