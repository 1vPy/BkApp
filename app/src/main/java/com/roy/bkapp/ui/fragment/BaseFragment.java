package com.roy.bkapp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roy.bkapp.BkApplication;
import com.roy.bkapp.dagger.component.ActivityComponent;
import com.roy.bkapp.dagger.component.DaggerActivityComponent;
import com.roy.bkapp.dagger.component.DaggerFragmentComponent;
import com.roy.bkapp.dagger.component.FragmentComponent;
import com.roy.bkapp.dagger.module.ActivityModule;
import com.roy.bkapp.dagger.module.FragmentModule;
import com.roy.bkapp.presenter.BasePresenter;
import com.roy.bkapp.ui.fragment.movie.MovieFragment;
import com.roy.bkapp.utils.LogUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 1vPy(Roy) on 2017/5/10.
 */

public abstract class BaseFragment<V, T extends BasePresenter<V>> extends RootFragment{

    @Inject
    protected T mPresenter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initInject();
        mPresenter.attachView((V) this);
        super.onViewCreated(view, savedInstanceState);

    }
    protected FragmentComponent getFragmentComponent(){
        return DaggerFragmentComponent.builder()
                .appComponent(BkApplication.getAppComponent())
                .fragmentModule(new FragmentModule(this))
                .build();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }


    protected abstract void initInject();
}
