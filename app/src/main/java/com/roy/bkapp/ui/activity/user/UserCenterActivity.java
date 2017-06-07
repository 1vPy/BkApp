package com.roy.bkapp.ui.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kyleduo.switchbutton.SwitchButton;
import com.roy.bkapp.R;
import com.roy.bkapp.presenter.user.UserCenterPresenter;
import com.roy.bkapp.ui.activity.BaseSwipeBackActivity;
import com.roy.bkapp.ui.view.user.UserCenterView;
import com.roy.bkapp.utils.ImageUtils;
import com.roy.bkapp.utils.ToastUtils;
import com.roy.bkapp.utils.UserPreference;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/5/22.
 */

public class UserCenterActivity extends BaseSwipeBackActivity<UserCenterView, UserCenterPresenter>
        implements UserCenterView
        , View.OnClickListener
        , CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.btn_logout)
    AppCompatButton btn_logout;
    @BindView(R.id.user_info)
    LinearLayout user_info;
    @BindView(R.id.user_header)
    ImageView user_header;
    @BindView(R.id.user_name)
    TextView user_name;
    @BindView(R.id.sbtn_msg)
    SwitchButton sbtn_msg;
    @BindView(R.id.sbtn_sync)
    SwitchButton sbtn_sync;

    public static void start(Context context) {
        Intent intent = new Intent(context, UserCenterActivity.class);
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
        mToolbar.setTitle(R.string.user_center);
        mToolbar.setNavigationOnClickListener(v -> UserCenterActivity.this.finish());

        if(!UserPreference.getUserPreference(this).readUserHeader().isEmpty()){
            ImageUtils.displayImage(this,UserPreference.getUserPreference(this).readUserHeader(),user_header);
        }else{
            ImageUtils.displayImage(this,R.drawable.avatar_default,user_header);
        }

        user_name.setText(UserPreference.getUserPreference(this).readUserInfo().getUsername());

        btn_logout.setOnClickListener(this);
        sbtn_msg.setOnCheckedChangeListener(this);
        sbtn_sync.setOnCheckedChangeListener(this);
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                UserPreference.getUserPreference(this).clearUserInfo();
                this.finish();
                break;
            case R.id.user_info:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.sbtn_msg:
                if(isChecked){
                    ToastUtils.showToast(R.string.msg_push_open);
                }else{
                    ToastUtils.showToast(R.string.msg_push_close);
                }
                break;
            case R.id.sbtn_sync:
                if(isChecked){
                    ToastUtils.showToast(R.string.collection_sync_open);
                }else{
                    ToastUtils.showToast(R.string.collection_sync_open);
                }
                break;
        }
    }
}
