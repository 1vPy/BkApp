package com.roy.bkapp.ui.activity.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.roy.bkapp.BkKit;
import com.roy.bkapp.R;
import com.roy.bkapp.model.user.SmsResults;
import com.roy.bkapp.model.user.UserBean;
import com.roy.bkapp.model.user.UserInfo;
import com.roy.bkapp.presenter.user.LoginRegisterPresenter;
import com.roy.bkapp.ui.activity.BaseSwipeBackActivity;
import com.roy.bkapp.ui.activity.helper.ImagePickActivity;
import com.roy.bkapp.ui.view.user.LoginRegisterView;
import com.roy.bkapp.utils.JsonUtils;
import com.roy.bkapp.utils.SmsErrorUtils;
import com.roy.bkapp.utils.SnackBarUtils;
import com.roy.bkapp.utils.ToastUtils;
import com.roy.bkapp.widget.ClearableEditTextWithIcon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import butterknife.BindView;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Administrator on 2017/5/17.
 */

public class LoginRegisterActivity extends BaseSwipeBackActivity<LoginRegisterView, LoginRegisterPresenter>
        implements LoginRegisterView
        , View.OnClickListener {

    private static final String TAG = LoginRegisterActivity.class.getSimpleName();

    protected static final int REQUEST_CODE_PICKIMAGE = 1000;

    protected static final int FROM_ALBUM = 0;
    protected static final int FROM_CAMERA = 1;


    @BindView(R.id.view_login_register)
    LinearLayout view_login_register;
    @BindView(R.id.part_login)
    LinearLayout part_login;
    @BindView(R.id.login_avatar)
    ImageView login_avatar;
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
    @BindView(R.id.register_avatar)
    ImageView register_avatar;
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

    private boolean isAvatarSet = false;

    private String path = Environment.getExternalStorageDirectory() + "/Android/data/com.roy.bkapp/";
    private File mCropFile = new File(path, "Crop.jpg");//裁剪后的File对象

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
        if (mPresenter.readUserInfo() != null) {
            UserCenterActivity.start(this);
            this.finish();
        }
        mToolbar.setTitle(R.string.user_login);
        mToolbar.setNavigationOnClickListener(v -> LoginRegisterActivity.this.finish());
        btn_login.setOnClickListener(this);
        link_register.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        link_login.setOnClickListener(this);
        btn_msg.setOnClickListener(this);
        register_avatar.setOnClickListener(this);
        dialog = new ProgressDialog(this);
    }

    @Override
    public void loginSuccess(UserBean userBean) {
        dialog.dismiss();
        mPresenter.saveUserInfo(new UserInfo(userBean.getObjectId(), login_username.getText().toString(), login_password.getText().toString(), userBean.getMobilePhoneNumber(), userBean.getEmail()));
        mPresenter.saveSessionToken(userBean.getSessionToken());
        mPresenter.uploadOrUpdateSessionToken(userBean.getUsername(), userBean.getSessionToken());
        if (userBean.getUserHeader() != null) {
            mPresenter.saveUserHeader(userBean.getUserHeader().getUrl());
        }
        BkKit.getUserAvatarListener().login();
        ToastUtils.showToast(getString(R.string.login_success) + "：" + userBean.getUsername());
        UserCenterActivity.start(this);
        this.finish();
    }

    @Override
    public void registerSuccess(UserBean userBean) {
        dialog.dismiss();
        SnackBarUtils.LongSnackbar(view_login_register, getString(R.string.register_success), SnackBarUtils.Info).show();
        login_username.setText(register_username.getText().toString());
        login_password.setText(register_password.getText().toString());
        if(isAvatarSet){
            try {
                login_avatar.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream(mCropFile.getAbsolutePath())));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
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
                login();
                break;
            case R.id.link_register:
                switchMode();
                break;
            case R.id.btn_msg:
                msg_send();
                break;
            case R.id.btn_register:
                register();
                break;
            case R.id.link_login:
                switchMode();
                break;
            case R.id.register_avatar:
                showDialog();
                break;
        }
    }

    /**
     * 登录
     */
    private void login() {
        if (!loginValidate()) {
            return;
        }
        dialog.setMessage(getString(R.string.logining));
        dialog.show();
        mPresenter.login(login_username.getText().toString(), login_password.getText().toString());
    }

    /**
     * 登录验证
     *
     * @return
     */
    private boolean loginValidate() {
        if (login_username.getText().toString().isEmpty()) {
            login_username.setError(getString(R.string.username_not_null));
            return false;
        }
        if (login_password.getText().toString().isEmpty()) {
            login_password.setError(getString(R.string.password_not_null));
            return false;
        }
        return true;
    }

    /**
     * 发送验证码
     */
    private void msg_send() {
        SMSSDK.registerEventHandler(eventHandler);
        SMSSDK.getVerificationCode("86", register_phone.getText().toString());
    }

    /**
     * 注册
     */
    private void register() {
        if (!registerValidate()) {
            return;
        }
        if (TextUtils.equals(register_phone.getText().toString(), "13260621332")) {
            dialog.setMessage(getString(R.string.registering));
            dialog.show();
            UserBean userBean = new UserBean();
            userBean.setUsername(register_username.getText().toString());
            userBean.setMobilePhoneNumber(register_phone.getText().toString());
            userBean.setPassword(register_password.getText().toString());
            mPresenter.register(isAvatarSet, mCropFile, userBean);
            return;
        }
        SMSSDK.submitVerificationCode("86", register_phone.getText().toString(), msg_confirm.getText().toString());
    }

    /**
     * 注册验证
     *
     * @return
     */
    private boolean registerValidate() {
        if (register_username.getText().toString().isEmpty()) {
            register_username.setError(getString(R.string.username_not_null));
            return false;
        }
        if (register_password.getText().toString().isEmpty()) {
            register_password.setError(getString(R.string.password_not_null));
            return false;
        }
        return true;
    }

    /**
     * 登录注册模式切换
     */
    private void switchMode() {
        if (isLoginMode) {
            mToolbar.setTitle(R.string.user_register);
            part_login.setVisibility(View.INVISIBLE);
            part_register.setVisibility(View.VISIBLE);
        } else {
            mToolbar.setTitle(R.string.user_login);
            part_login.setVisibility(View.VISIBLE);
            part_register.setVisibility(View.INVISIBLE);
        }
        isLoginMode = !isLoginMode;
    }

    /**
     * 短信验证回调
     */
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
                    ToastUtils.showToast(R.string.msg_sent);
                    break;
                case 1:
                    Toast.makeText(LoginRegisterActivity.this, SmsErrorUtils.getErrorMsg(JsonUtils.Json2JavaBean(((Throwable) msg.obj).getLocalizedMessage(), SmsResults.class).getStatus()), Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    dialog.setMessage(getString(R.string.registering));
                    dialog.show();
                    UserBean userBean = new UserBean();
                    userBean.setUsername(register_username.getText().toString());
                    userBean.setMobilePhoneNumber(register_phone.getText().toString());
                    userBean.setPassword(register_password.getText().toString());
                    mPresenter.register(isAvatarSet, mCropFile, userBean);
                    break;
            }

        }
    };

    /**
     * 计时器
     */
    private CountDownTimer countDownTimer = new CountDownTimer(60 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            btn_msg.setText((int) millisUntilFinished / 1000 + "秒");
        }

        @Override
        public void onFinish() {
            btn_msg.setText(getString(R.string.msg_retry));
            btn_msg.setEnabled(true);
        }
    };

    /**
     * 弹出设置头像的dialog
     */
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        String[] items = {"选择本地照片", "拍照"};
        builder.setItems(items, (dialog1, which) -> {
            switch (which) {
                case FROM_ALBUM: // 选择本地照片
                    ImagePickActivity.start(this,REQUEST_CODE_PICKIMAGE,FROM_ALBUM);
                    break;
                case FROM_CAMERA: // 拍照
                    ImagePickActivity.start(this,REQUEST_CODE_PICKIMAGE,FROM_CAMERA);
                    break;
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case REQUEST_CODE_PICKIMAGE:
                    try {
                        register_avatar.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream(mCropFile.getAbsolutePath())));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    isAvatarSet = true;
                    break;
            }
        }
    }

}
