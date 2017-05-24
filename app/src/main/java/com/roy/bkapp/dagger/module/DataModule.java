package com.roy.bkapp.dagger.module;

import android.content.Context;

import com.roy.bkapp.db.DbHelperService;
import com.roy.bkapp.db.DbHelper;
import com.roy.bkapp.utils.PreferencesHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 1vPy(Roy) on 2017/5/12.
 */


@Module
public class DataModule {

    @Singleton
    @Provides
    PreferencesHelper providePreferencesHelper(Context applicationContext){
        return new PreferencesHelper(applicationContext);
    };

    @Singleton
    @Provides
    DbHelper provideDbHelper(Context context){
        return new DbHelper(context);
    }

    @Singleton
    @Provides
    DbHelperService provideDbHelperService(DbHelper dbHelper){
        return new DbHelperService(dbHelper);
    }
}
