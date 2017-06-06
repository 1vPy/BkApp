package com.roy.bkapp.presenter.user;

import com.roy.bkapp.http.RequestCallback;
import com.roy.bkapp.http.api.bmob.BmobApiService;
import com.roy.bkapp.model.user.UserBean;
import com.roy.bkapp.presenter.BasePresenter;
import com.roy.bkapp.ui.view.user.LoginRegisterView;
import com.roy.bkapp.utils.LogUtils;

import javax.inject.Inject;

/**
 * Created by Administrator on 2017/5/17.
 */

public class LoginRegisterPresenter extends BasePresenter<LoginRegisterView> {
    private static final String TAG = LoginRegisterPresenter.class.getSimpleName();
    private BmobApiService mBmobApiService;

    @Inject
    public LoginRegisterPresenter(BmobApiService bmobApiService) {
        mBmobApiService = bmobApiService;
    }

    public void login(String username, String password) {
        mBmobApiService.login(username, password, new RequestCallback<UserBean>() {
            @Override
            public void onSuccess(UserBean userBean) {
                if (isAttached()) {
                    getView().loginSuccess(userBean);
                }
            }

            @Override
            public void onFailure(String message) {
                if (isAttached()) {
                    getView().showError(message);
                }
            }
        });
    }

    public void register(UserBean userBean) {
        mBmobApiService.register(userBean, new RequestCallback<UserBean>() {
            @Override
            public void onSuccess(UserBean userBean) {
                if (isAttached()) {
                    getView().registerSuccess(userBean);
                }
            }

            @Override
            public void onFailure(String message) {
                if (isAttached()) {
                    getView().showError(message);
                }
            }
        });
    }

    public void uploadOrUpdateSessionToken(String username, String sessionToken) {
        mBmobApiService.uploadOrUpdateSessionToken(username, sessionToken, new RequestCallback<String>() {
            @Override
            public void onSuccess(String s) {
                LogUtils.log(TAG, s, LogUtils.DEBUG);
            }

            @Override
            public void onFailure(String message) {
                LogUtils.log(TAG, message, LogUtils.DEBUG);
            }
        });
    }
}
