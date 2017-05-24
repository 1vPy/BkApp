package com.roy.bkapp.presenter;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by 1vPy(Roy) on 2017/5/10.
 */

public class BasePresenter<T> {

    protected Reference<T> mViewRef;

    public void attachView(T view){
        mViewRef = new WeakReference<T>(view);
    }

    protected boolean isAttached(){
        return mViewRef != null && mViewRef.get() != null;
    }

    protected T getView(){
        return mViewRef.get();
    }

    public void detachView(){
        if(mViewRef != null){
            mViewRef.clear();
            mViewRef = null;
        }
    }
}
