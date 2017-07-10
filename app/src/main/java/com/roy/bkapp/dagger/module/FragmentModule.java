package com.roy.bkapp.dagger.module;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.roy.bkapp.dagger.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 1vPy(Roy) on 2017/5/11.
 */

@Module
public class FragmentModule {

    private Fragment mFragment;

    public FragmentModule(Fragment fragment){
        mFragment = fragment;
    }

    @FragmentScope
    @Provides
    public Activity provideFragment(){
        return mFragment.getActivity();
    }
}
