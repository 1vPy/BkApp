package com.roy.bkapp;

import android.app.Application;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.roy.bkapp.dagger.component.AppComponent;
import com.roy.bkapp.dagger.component.DaggerAppComponent;
import com.roy.bkapp.dagger.module.ApplicationModule;
import com.roy.bkapp.dagger.module.DataModule;
import com.roy.bkapp.dagger.module.HttpModule;
import com.roy.bkapp.http.RequestCallback;
import com.roy.bkapp.model.user.login_config.LoginConfig;
import com.roy.bkapp.utils.LogUtils;
import com.roy.bkapp.utils.UserPreference;

import cn.smssdk.SMSSDK;

/**
 * Created by 1vPy(Roy) on 2017/5/10.
 */

public class BkApplication extends Application {
    private static final String TAG = BkApplication.class.getSimpleName();

    private static AppComponent appComponent;
    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();

    }

    private void init() {
        initBkKit();
        initShareSDK();
        initSmsSDK();
        initCrashHandler();
    }

    private void initShareSDK() {

    }

    private void initSmsSDK() {
        ApplicationInfo appInfo = null;
        try {
            appInfo = this.getPackageManager()
                    .getApplicationInfo(getPackageName(),
                            PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String key = appInfo.metaData.getString("sms_app_key");
        String secret = appInfo.metaData.getString("sms_app_secret");
        LogUtils.log(TAG, "key = " + key + ",secret = " + secret, LogUtils.DEBUG);
        SMSSDK.initSDK(getApplicationContext(), key, secret);
    }

    private void initBkKit() {
        BkKit.setContext(getApplicationContext());
    }

    private void initCrashHandler() {
        CrashHandler.getInstance().init(getApplicationContext());
    }

    public static AppComponent getAppComponent() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .applicationModule(new ApplicationModule(instance))
                    .httpModule(new HttpModule())
                    .dataModule(new DataModule())
                    .build();
        }
        return appComponent;
    }

}
