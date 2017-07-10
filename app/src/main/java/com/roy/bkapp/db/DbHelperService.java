package com.roy.bkapp.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.roy.bkapp.http.RequestCallback;
import com.roy.bkapp.model.collection.MovieCollection;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 1vPy(Roy) on 2017/5/12.
 */

public class DbHelperService {

    private DbHelper mDbHelper;

    @Inject
    public DbHelperService(DbHelper dbHelper) {
        mDbHelper = dbHelper;
    }

    public void insertCollection(final MovieCollection collection) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.beginTransaction();
        db.execSQL("insert into tb_collection values(null,?,?,?,?)", new Object[]{collection.getImageUrl(), collection.getMovieName(), collection.getMovieId(), collection.getIsSync()});
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public void deleteCollection(final String movieId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.beginTransaction();
        db.execSQL("delete from tb_collection where movieId = ?", new Object[]{movieId});
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public boolean selectCollection(final String movieId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from tb_collection where movieId = ?", new String[]{movieId});
        if (cursor.getCount() == 0) {
            return false;
        }
        cursor.close();
        db.close();
        return true;
    }

    public MovieCollection searchCollection(final String movieId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        MovieCollection movieCollection = new MovieCollection();
        Cursor cursor = db.rawQuery("select * from tb_collection where movieId = ?", new String[]{movieId});
        while (cursor.moveToNext()) {
            movieCollection.setId(cursor.getInt(cursor.getColumnIndex("id")));
            movieCollection.setImageUrl(cursor.getString(cursor.getColumnIndex("imageUrl")));
            movieCollection.setMovieName(cursor.getString(cursor.getColumnIndex("movieName")));
            movieCollection.setMovieId(cursor.getString(cursor.getColumnIndex("movieId")));
            movieCollection.setIsSync(cursor.getInt(cursor.getColumnIndex("isSync")));
        }
        cursor.close();
        db.close();
        return movieCollection;
    }


    public List<MovieCollection> selectAllCollection() {
        List<MovieCollection> movieCollectionList = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from tb_collection", null);
        while (cursor.moveToNext()) {
            MovieCollection movieCollection = new MovieCollection();
            movieCollection.setId(cursor.getInt(cursor.getColumnIndex("id")));
            movieCollection.setImageUrl(cursor.getString(cursor.getColumnIndex("imageUrl")));
            movieCollection.setMovieName(cursor.getString(cursor.getColumnIndex("movieName")));
            movieCollection.setMovieId(cursor.getString(cursor.getColumnIndex("movieId")));
            movieCollection.setIsSync(cursor.getInt(cursor.getColumnIndex("isSync")));
            movieCollectionList.add(movieCollection);
        }
        cursor.close();
        db.close();
        return movieCollectionList;
    }


    public void toggleSyncMovie(String movieId, boolean isSync) {
        if (isSync) {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            db.beginTransaction();
            db.execSQL("update tb_collection set isSync = 1 where movieId = ?", new Object[]{movieId});
            db.setTransactionSuccessful();
            db.endTransaction();
        } else {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            db.beginTransaction();
            db.execSQL("update tb_collection set isSync = 0 where movieId = ?", new Object[]{movieId});
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }
}
