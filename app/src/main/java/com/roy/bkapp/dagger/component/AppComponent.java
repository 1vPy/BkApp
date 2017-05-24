package com.roy.bkapp.dagger.component;

import android.content.Context;

import com.roy.bkapp.dagger.module.ApplicationModule;
import com.roy.bkapp.dagger.module.DataModule;
import com.roy.bkapp.dagger.module.HttpModule;
import com.roy.bkapp.db.DbHelper;
import com.roy.bkapp.http.api.baidu.BaiduApiService;
import com.roy.bkapp.http.api.bmob.BmobApiService;
import com.roy.bkapp.http.api.douban.DoubanApiService;
import com.roy.bkapp.utils.PreferencesHelper;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by 1vPy(Roy) on 2017/5/10.
 */

@Singleton
@Component(modules = {ApplicationModule.class,HttpModule.class, DataModule.class})
public interface AppComponent {

    Context getContext();

    DoubanApiService getDoubanApiService();

    BaiduApiService getBaiduApiService();

    BmobApiService getBmobApiService();

    PreferencesHelper getPreferencesHelper();

    DbHelper getDbHelper();
}
