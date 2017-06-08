package com.roy.bkapp.presenter.user;

import com.roy.bkapp.http.RequestCallback;
import com.roy.bkapp.http.api.bmob.BmobApiService;
import com.roy.bkapp.model.user.UserInfo;
import com.roy.bkapp.presenter.BasePresenter;
import com.roy.bkapp.ui.view.user.UserCenterView;
import com.roy.bkapp.utils.UserPreference;

import javax.inject.Inject;

/**
 * Created by Administrator on 2017/5/22.
 */

public class UserCenterPresenter extends BasePresenter<UserCenterView>{

    private BmobApiService mBmobApiService;
    private UserPreference mUserPreference;

    @Inject
    public UserCenterPresenter(BmobApiService bmobApiService,UserPreference userPreference){
        mBmobApiService = bmobApiService;
        mUserPreference = userPreference;
    }
    public UserInfo readUserInfo(){
        return mUserPreference.readUserInfo();
    }

    public void saveUserInfo(UserInfo userInfo){
        mUserPreference.saveUserInfo(userInfo);
    }

    public void saveSessionToken(String sessionToken){
        mUserPreference.saveSessionToken(sessionToken);
    }

    public void saveUserHeader(String url){
        mUserPreference.saveUserHeader(url);
    }

    public String readUserHeader(){
        return mUserPreference.readUserHeader();
    }

    public void clearUserInfo(){
        mUserPreference.clearUserInfo();
    }

    public void modifyAvatar(String path){
        mBmobApiService.modifyAvatar(path, new RequestCallback<String>() {
            @Override
            public void onSuccess(String s) {
                if(isAttached()){
                    getView().modifyAvatarSuccess(s);
                }
            }

            @Override
            public void onFailure(String message) {
                if(isAttached()){
                    getView().showError(message);
                }
            }
        });
    }

}
