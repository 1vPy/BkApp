package com.roy.bkapp;

import android.content.Context;

import com.roy.bkapp.ui.listener.UserAvatarListener;

/**
 * Created by 1vPy(Roy) on 2017/5/10.
 */

public class BkKit {
    private static Context mContext;

    private static UserAvatarListener mUserAvatarListener;

    public static void setContext(Context context){
        mContext = context;
    }

    public static Context getContext(){
        return mContext;
    }

    public static void setUserAvatarListener(UserAvatarListener userAvatarListener){
        mUserAvatarListener = userAvatarListener;
    }

    public static UserAvatarListener getUserAvatarListener(){
        return mUserAvatarListener;
    }
}
