package com.roy.bkapp.ui.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.roy.bkapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 1vPy(Roy) on 2017/5/11.
 */

public abstract class RootActivity extends AppCompatActivity {
    @BindView(R.id.common_toolbar)
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        initToolBar();
        initViewAndEvent();
    }
    public void initToolBar() {
        if (mToolbar != null) {
            mToolbar.setTitle("");
            setSupportActionBar(mToolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected abstract void initViewAndEvent();
}
