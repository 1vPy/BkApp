package com.roy.bkapp.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.jude.swipbackhelper.SwipeBackHelper;
import com.roy.bkapp.presenter.BasePresenter;

/**
 * Created by 1vPy(Roy) on 2017/5/10.
 */

public abstract class BaseSwipeBackActivity<V,T extends BasePresenter<V>> extends BaseActivity<V,T>{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBackHelper.onCreate(this);
        SwipeBackHelper.getCurrentPage(this)//get current instance
                .setSwipeBackEnable(true)
                .setSwipeEdgePercent(0.2f)//0.2 mean left 20% of screen can touch to begin swipe.
                .setSwipeSensitivity(1)//sensitiveness of the gesture。0:slow  1:sensitive
                .setScrimColor(Color.TRANSPARENT)//color of Scrim below the activity
                .setClosePercent(0.8f)//close activity when swipe over this
                .setSwipeRelateEnable(true)//if should move together with the following Activity
                .setSwipeRelateOffset(500)//the Offset of following Activity when setSwipeRelateEnable(true)
                .setDisallowInterceptTouchEvent(false);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }
}
