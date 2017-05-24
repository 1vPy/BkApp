package com.roy.bkapp.dagger.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2017/5/14.
 */

@Module
public class ApplicationModule {

    private Application mApplication;

    public ApplicationModule(Application application){
        mApplication = application;
    }

    @Singleton
    @Provides
    public Context provideContext(){
        return mApplication.getApplicationContext();
    }
}
