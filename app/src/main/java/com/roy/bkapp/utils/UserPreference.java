package com.roy.bkapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.roy.bkapp.model.user.UserInfo;

/**
 * Created by Administrator on 2017/5/22.
 */

public class UserPreference {
    private static final String TAG = UserPreference.class.getSimpleName();
    private final static String SP_NAME = "UserInfo";
    private final static int MODE = Context.MODE_PRIVATE;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static UserPreference instance = null;

    private UserPreference(Context context) {
        preferences = context.getSharedPreferences(SP_NAME, MODE);
        editor = preferences.edit();
    }

    public static UserPreference getUserPreference(Context context) {
        if (instance == null) {
            synchronized (UserPreference.class) {
                if (instance == null) {
                    instance = new UserPreference(context);
                }
            }
        }
        return instance;
    }

    public void saveUserInfo(UserInfo userInfo) {
        editor.putString("username", userInfo.getUsername());
        editor.putString("password", userInfo.getPassword());
        editor.commit();
    }

    public UserInfo readUserInfo() {
        String username = preferences.getString("username", "");
        String password = preferences.getString("password", "");
        LogUtils.log(TAG, "username:" + username + ",password:" + password, LogUtils.DEBUG);
        if (!username.isEmpty() && !password.isEmpty()) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUsername(username);
            userInfo.setPassword(password);
            return userInfo;
        }
        return null;
    }

    public void clearUserInfo() {
        editor.clear();
        editor.commit();
    }

}
