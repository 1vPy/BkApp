package com.roy.bkapp.presenter;

import com.roy.bkapp.model.user.UserInfo;
import com.roy.bkapp.ui.view.MainView;
import com.roy.bkapp.utils.UserPreference;

import javax.inject.Inject;

/**
 * Created by 1vPy(Roy) on 2017/6/7.
 */

public class MainPresenter extends BasePresenter<MainView>{

    private UserPreference mUserPreference;

    @Inject
    public MainPresenter(UserPreference userPreference){
        mUserPreference = userPreference;
    }

    public UserInfo readUserInfo(){
        return mUserPreference.readUserInfo();
    }

    public String readSessionToken(){
        return mUserPreference.readSessionToken();
    }

    public String readUserHeader(){
        return mUserPreference.readUserHeader();
    }

    public void clearUserInfo(){
        mUserPreference.clearUserInfo();
    }
}
