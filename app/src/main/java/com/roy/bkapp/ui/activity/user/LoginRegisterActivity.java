package com.roy.bkapp.ui.activity.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.roy.bkapp.R;
import com.roy.bkapp.model.user.SmsResults;
import com.roy.bkapp.model.user.UserBean;
import com.roy.bkapp.model.user.UserInfo;
import com.roy.bkapp.presenter.user.LoginRegisterPresenter;
import com.roy.bkapp.ui.activity.BaseSwipeBackActivity;
import com.roy.bkapp.ui.view.user.LoginRegisterView;
import com.roy.bkapp.utils.JsonUtils;
import com.roy.bkapp.utils.SmsErrorUtils;
import com.roy.bkapp.utils.SnackBarUtils;
import com.roy.bkapp.utils.UserPreference;
import com.roy.bkapp.widget.ClearableEditTextWithIcon;

import butterknife.BindView;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static com.mob.tools.log.MobUncaughtExceptionHandler.register;

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
    @BindView(R.id.msg_confirm)
    ClearableEditTextWithIcon msg_confirm;
    @BindView(R.id.btn_msg)
    AppCompatButton btn_msg;
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
        mToolbar.setNavigationOnClickListener(v -> LoginRegisterActivity.this.finish());
        btn_login.setOnClickListener(this);
        link_register.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        link_login.setOnClickListener(this);
        btn_msg.setOnClickListener(this);
        dialog = new ProgressDialog(this);
    }

    @Override
    public void loginSuccess(UserBean userBean) {
        dialog.dismiss();
        UserPreference.getUserPreference(this).saveUserInfo(new UserInfo(userBean.getObjectId(), login_username.getText().toString(), login_password.getText().toString()));
        UserPreference.getUserPreference(this).saveSessionToken(userBean.getSessionToken());
        mPresenter.uploadOrUpdateSessionToken(userBean.getUsername(), userBean.getSessionToken());
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
        register_username.setText("");
        register_phone.setText("");
        register_password.setText("");
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
            case R.id.btn_msg:
                SMSSDK.registerEventHandler(eventHandler);
                SMSSDK.getVerificationCode("86", register_phone.getText().toString());
                break;
            case R.id.btn_register:
                SMSSDK.submitVerificationCode("86", register_phone.getText().toString(), msg_confirm.getText().toString());
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

    private EventHandler eventHandler = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            super.afterEvent(event, result, data);
            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //提交验证码成功
                    Message message = new Message();
                    message.what = 2;
                    mHandler.sendMessage(message);
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //获取验证码成功
                    countDownTimer.start();
                    Message message = new Message();
                    message.what = 0;
                    mHandler.sendMessage(message);

                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    //返回支持发送验证码的国家列表
                }
            } else {
                ((Throwable) data).printStackTrace();
                Message message = new Message();
                message.what = 1;
                message.obj = data;
                mHandler.sendMessage(message);
            }
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    btn_msg.setEnabled(false);
                    Toast.makeText(LoginRegisterActivity.this, "验证码已经发送",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(LoginRegisterActivity.this, SmsErrorUtils.getErrorMsg(JsonUtils.Json2JavaBean(((Throwable) msg.obj).getLocalizedMessage(), SmsResults.class).getStatus()), Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    dialog.setMessage("正在注册...");
                    dialog.show();
                    UserBean userBean = new UserBean();
                    userBean.setUsername(register_username.getText().toString());
                    userBean.setMobilePhoneNumber(register_phone.getText().toString());
                    userBean.setPassword(register_password.getText().toString());
                    mPresenter.register(userBean);
                    break;
            }

        }
    };

    private CountDownTimer countDownTimer = new CountDownTimer(60 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            btn_msg.setText((int) millisUntilFinished / 1000 + "秒");
        }

        @Override
        public void onFinish() {
            btn_msg.setText("重新获取");
            btn_msg.setEnabled(true);
        }
    };
}
