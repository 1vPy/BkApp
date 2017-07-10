package com.roy.bkapp.dagger.module;

import android.app.Activity;

import com.roy.bkapp.dagger.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 1vPy(Roy) on 2017/5/10.
 */


@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @ActivityScope
    @Provides
    public Activity provideActivity() {
        return mActivity;
    }
}
