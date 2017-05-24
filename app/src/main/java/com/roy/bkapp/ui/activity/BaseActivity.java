package com.roy.bkapp.ui.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.roy.bkapp.BkApplication;
import com.roy.bkapp.dagger.component.ActivityComponent;
import com.roy.bkapp.dagger.component.DaggerActivityComponent;
import com.roy.bkapp.dagger.module.ActivityModule;
import com.roy.bkapp.presenter.BasePresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by 1vPy(Roy) on 2017/5/10.
 */

public abstract class BaseActivity<V, T extends BasePresenter<V>> extends RootActivity{

    @Inject
    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInject();
        mPresenter.attachView((V) this);

    }

    protected ActivityComponent getActivityComponent(){
        return DaggerActivityComponent.builder()
                .appComponent(BkApplication.getAppComponent())
                .activityModule(new ActivityModule(this))
                .build();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    protected abstract void initInject();
}
