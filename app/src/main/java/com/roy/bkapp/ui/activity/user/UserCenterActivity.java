package com.roy.bkapp.ui.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.roy.bkapp.R;
import com.roy.bkapp.presenter.user.UserCenterPresenter;
import com.roy.bkapp.ui.activity.BaseSwipeBackActivity;
import com.roy.bkapp.ui.view.user.UserCenterView;
import com.roy.bkapp.utils.UserPreference;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/5/22.
 */

public class UserCenterActivity extends BaseSwipeBackActivity<UserCenterView, UserCenterPresenter> implements
        UserCenterView
        , View.OnClickListener {
    @BindView(R.id.btn_logout)
    AppCompatButton btn_logout;


    public static void start(Context context){
        Intent intent = new Intent(context,UserCenterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercenter);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViewAndEvent() {
        mToolbar.setTitle("用户中心");
        mToolbar.setNavigationOnClickListener(v -> UserCenterActivity.this.finish());
        btn_logout.setOnClickListener(this);
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_logout:
                UserPreference.getUserPreference(this).clearUserInfo();
                this.finish();
                break;
        }
    }
}
