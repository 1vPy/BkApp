package com.roy.bkapp.ui.view.user;

import com.roy.bkapp.model.user.ErrorBean;
import com.roy.bkapp.model.user.UserBean;
import com.roy.bkapp.ui.view.BaseView;

/**
 * Created by Administrator on 2017/5/17.
 */

public interface LoginRegisterView extends BaseView{
    void loginSuccess(UserBean userBean);

    void registerSuccess(UserBean userBean);
}
