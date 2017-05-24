package com.roy.bkapp;

import android.content.Context;

/**
 * Created by 1vPy(Roy) on 2017/5/10.
 */

public class BkKit {
    private static Context mContext;

    public static void setContext(Context context){
        mContext = context;
    }

    public static Context getContext(){
        return mContext;
    }
}
