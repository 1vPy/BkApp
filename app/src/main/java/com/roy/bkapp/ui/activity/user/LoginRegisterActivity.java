package com.roy.bkapp.ui.activity.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.roy.bkapp.R;
import com.roy.bkapp.model.user.UserBean;
import com.roy.bkapp.model.user.UserInfo;
import com.roy.bkapp.presenter.user.LoginRegisterPresenter;
import com.roy.bkapp.ui.activity.BaseSwipeBackActivity;
import com.roy.bkapp.ui.view.user.LoginRegisterView;
import com.roy.bkapp.utils.SnackBarUtils;
import com.roy.bkapp.utils.UserPreference;
import com.roy.bkapp.widget.ClearableEditTextWithIcon;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/5/17.
 */

public class LoginRegisterActivity extends BaseSwipeBackActivity<LoginRegisterView, LoginRegisterPresenter>
        implements LoginRegisterView
        , View.OnClickListener {
    @BindView(R.id.view_login_register)
    LinearLayout view_login_register;
    @BindView(R.id.part_login)
    LinearLayout part_login;
    @BindView(R.id.login_username)
    ClearableEditTextWithIcon login_username;
    @BindView(R.id.login_password)
    ClearableEditTextWithIcon login_password;
    @BindView(R.id.btn_login)
    AppCompatButton btn_login;
    @BindView(R.id.link_register)
    TextView link_register;

    @BindView(R.id.part_register)
    LinearLayout part_register;
    @BindView(R.id.register_username)
    ClearableEditTextWithIcon register_username;
    @BindView(R.id.register_phone)
    ClearableEditTextWithIcon register_phone;
    @BindView(R.id.register_password)
    ClearableEditTextWithIcon register_password;
    @BindView(R.id.btn_register)
    AppCompatButton btn_register;
    @BindView(R.id.link_login)
    TextView link_login;

    private ProgressDialog dialog;

    private boolean isLoginMode = true;

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginRegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViewAndEvent() {
        if (UserPreference.getUserPreference(this).readUserInfo() != null) {
            UserCenterActivity.start(this);
            this.finish();
        }
        mToolbar.setTitle("用户登录");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginRegisterActivity.this.finish();
            }
        });
        btn_login.setOnClickListener(this);
        link_register.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        link_login.setOnClickListener(this);
        dialog = new ProgressDialog(this);
    }

    @Override
    public void loginSuccess(UserBean userBean) {
        dialog.dismiss();
        UserPreference.getUserPreference(this).saveUserInfo(new UserInfo(login_username.getText().toString(), login_password.getText().toString()));
        SnackBarUtils.LongSnackbar(view_login_register, "登陆成功：" + userBean.getUsername(), SnackBarUtils.Info).show();
        UserCenterActivity.start(this);
        this.finish();
    }

    @Override
    public void registerSuccess(UserBean userBean) {
        dialog.dismiss();
        SnackBarUtils.LongSnackbar(view_login_register, "注册成功", SnackBarUtils.Info).show();
        login_username.setText(register_username.getText().toString());
        login_password.setText(register_password.getText().toString());
        switchMode();
    }

    @Override
    public void showError(String message) {
        dialog.dismiss();
        SnackBarUtils.LongSnackbar(view_login_register, message, SnackBarUtils.Warning).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                dialog.setMessage("登录中...");
                dialog.show();
                mPresenter.login(login_username.getText().toString(), login_password.getText().toString());
                break;
            case R.id.link_register:
                switchMode();
                break;
            case R.id.btn_register:
                dialog.setMessage("正在注册...");
                dialog.show();
                UserBean userBean = new UserBean();
                userBean.setUsername(register_username.getText().toString());
                userBean.setMobilePhoneNumber(register_phone.getText().toString());
                userBean.setPassword(register_password.getText().toString());
                mPresenter.register(userBean);
                break;
            case R.id.link_login:
                switchMode();
                break;
        }
    }

    private void switchMode() {
        if (isLoginMode) {
            mToolbar.setTitle("用户注册");
            part_login.setVisibility(View.INVISIBLE);
            part_register.setVisibility(View.VISIBLE);
        } else {
            mToolbar.setTitle("用户登录");
            part_login.setVisibility(View.VISIBLE);
            part_register.setVisibility(View.INVISIBLE);
        }
        isLoginMode = !isLoginMode;
    }
}
