package com.roy.bkapp.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.roy.bkapp.http.RequestCallback;

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

    public void insertCollection(final String movieId, final RequestCallback<String> requestCallback) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                try{
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    db.beginTransaction();
                    db.execSQL("insert into tb_collection values(null,?)", new Object[]{movieId});
                    db.setTransactionSuccessful();
                    db.endTransaction();
                    db.close();
                    e.onNext("收藏成功");
                }catch (Exception e1){
                    e1.printStackTrace();
                    e.onError(e1);
                }

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        requestCallback.onSuccess(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        requestCallback.onFailure("收藏失败："+throwable.getLocalizedMessage());
                    }
                });
    }

    public void deleteCollection(final String movieId, final RequestCallback<String> requestCallback) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                try{
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    db.beginTransaction();
                    db.execSQL("delete from tb_collection where movieId = ?", new Object[]{movieId});
                    db.setTransactionSuccessful();
                    db.endTransaction();
                    db.close();
                    e.onNext("删除成功");
                }catch (Exception e1){
                    e1.printStackTrace();
                    e.onError(e1);
                }

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        requestCallback.onSuccess(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        requestCallback.onFailure("删除失败："+throwable.getLocalizedMessage());
                    }
                });
    }

    public void selectCollection(final String movieId, final RequestCallback<Boolean> requestCallback){
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                try{
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    Cursor cursor = db.rawQuery("select * from tb_collection where movieId = ?", new String[]{movieId});
                    if (cursor.getCount() == 0) {
                        e.onNext(false);
                        e.onComplete();
                    }
                    cursor.close();
                    db.close();
                    e.onNext(true);
                }catch (Exception e1){
                    e1.printStackTrace();
                    e.onError(e1);
                }

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        requestCallback.onSuccess(aBoolean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        requestCallback.onFailure("删除失败：" + throwable.getLocalizedMessage());
                    }
                });
    }
}
